package cr.ac.una.gps

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.PolyUtil
import cr.ac.una.gps.dao.PuntoPolyDao
import cr.ac.una.gps.entity.Ubicacion
import cr.ac.una.gps.dao.UbicacionDao
import cr.ac.una.gps.db.AppDatabase
import cr.ac.una.gps.entity.PuntoPoly
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var ubicacionDao: UbicacionDao
    private lateinit var puntoPolyDao: PuntoPolyDao
    private lateinit var locationReceiver: BroadcastReceiver
    private lateinit var ubicaciones: List<Ubicacion>
    private lateinit var puntosPoly: List<PuntoPoly>
    private var polygon: Polygon? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        polygon = null
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicaciones = ubicacionDao.getAll() as List<Ubicacion>


                withContext(Dispatchers.Main){
                    ubicaciones.forEach { ubicacion ->

                        val currentLatLng = LatLng(ubicacion.latitud, ubicacion.longitud)

                        map.addMarker(MarkerOptions().position(currentLatLng).title(ubicacion.nombre))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    }
                }
            }
        }

        createPolygon()





    }

    private fun createPolygon() {
        val polygonOptions = PolygonOptions()
        /*
        polygonOptions.add(LatLng(10.1365823,-84.4464617 ))
        polygonOptions.add(LatLng(9.6062242,-84.1718035))
        polygonOptions.add(LatLng(9.8606862,-83.616994 ))
        polygonOptions.add(LatLng(10.3690164, -83.9960223 ))
        polygonOptions.add(LatLng(10.1365823, -84.4464617 ))*/


        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                puntosPoly = puntoPolyDao.getAll() as List<PuntoPoly>
                println(puntosPoly.size)
                withContext(Dispatchers.Main){
                    puntosPoly.forEach { punto ->
                        polygonOptions.add(LatLng(punto.latitud, punto.longitud))
                    }
                    if(puntosPoly.size > 0){
                        polygon = map.addPolygon(polygonOptions)
                    }
                }
            }
        }






    }
    private fun isLocationInsidePolygon(location: LatLng): Boolean {
        return polygon != null && PolyUtil.containsLocation(location, polygon?.points, true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ubicacionDao = AppDatabase.getInstance(requireContext()).ubicacionDao()
        puntoPolyDao = AppDatabase.getInstance(requireContext()).puntoPolyDao()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap

            mapFragment.getMapAsync(callback)
        }
        iniciaServicio()


        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val latitud = intent?.getDoubleExtra("latitud", 0.0) ?: 0.0
                val longitud = intent?.getDoubleExtra("longitud", 0.0) ?: 0.0


                val currentLatLng = LatLng(latitud, longitud)

                println(latitud.toString() +"    " +longitud + "" + isLocationInsidePolygon(currentLatLng))

                map.addMarker(MarkerOptions().position(currentLatLng).title(Configuracion.textostatico.toString()))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 8f))


                val entity = Ubicacion(
                    id = null,
                    latitud = latitud,
                    longitud = longitud,
                    fecha = Date(),
                    nombre = Configuracion.textostatico,
                    inpoly = isLocationInsidePolygon(currentLatLng)
                )
                insertEntity(entity)


            }
        }
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))

    }

    override fun onResume() {
        super.onResume()
        // Registrar el receptor para recibir actualizaciones de ubicación
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el receptor al pausar el fragmento
        context?.unregisterReceiver(locationReceiver)
    }



    private fun insertEntity(entity: Ubicacion) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicacionDao.insert(entity)
            }
        }

    }



    private fun iniciaServicio() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            val intent = Intent(context, LocationService::class.java)
            context?.startService(intent)

        }


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    iniciaServicio()
                }
            } else {
                // Permiso denegado, maneja la situación de acuerdo a tus necesidades
            }
        }
    }
}
