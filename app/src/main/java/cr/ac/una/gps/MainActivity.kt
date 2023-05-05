package cr.ac.una.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import cr.ac.menufragment.AcercaDeFragment

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    //instancia de to-do el drawer layout
    lateinit var drawerLayout: DrawerLayout
    //atributo de clase de tipo drawer layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar =findViewById<Toolbar>(R.id.toolbar)
        //instancia del toolbar
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout) //esto es to-do el componente, contiene to-do lo de la vista
        var toogle = ActionBarDrawerToggle( //esto es lo que extiende el menu
            //asocia todos los componente
            this,
            drawerLayout, //la pantalla que tiene to-do
            toolbar, //boton que lo va a accionar
            R.string.drawer_open, //para abrir
            R.string.drawer_close //para cerrar
        ) //a eso hay que decirle que tiene un listener, hay que asociarle un listener a la actividad
        drawerLayout.addDrawerListener(toogle)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        toogle.syncState()


    }

    //esto es para cuando se presiona el boton back del telefono esconda el menu y no se cierre toda la aplicacion
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            //si el drawer de la actividad esta abierto lo cerramos
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            //sino simplemente se sale de la aplicacion
            super.onBackPressed()
    }


    //esto es para que cuando seleccione alguna opcion del navigation view
    //para eso es la herencia NavigationView.OnNavigationItemSelectedListener
    //OnNavigationItemSelectListener es la interface que se implementa cuando se selecciona una opcion
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        lateinit var fragment : Fragment

        when(item.itemId){
            R.id.home -> {
                fragment = HomeFragment.newInstance("hi","lool")

            }
            R.id.maps -> {
                fragment = MapsFragment()

            }
            R.id.Configuracion ->{
                fragment = Configuracion()
            }

            R.id.AcercaDe -> {
                fragment = AcercaDeFragment()

            }
            R.id.PolyConfig -> {
                fragment = Poly_config()
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_cont, fragment)
            .commit()

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}