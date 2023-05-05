package cr.ac.una.gps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import cr.ac.una.gps.dao.PuntoPolyDao
import cr.ac.una.gps.dao.UbicacionDao
import cr.ac.una.gps.db.AppDatabase
import cr.ac.una.gps.entity.PuntoPoly
import cr.ac.una.gps.entity.Ubicacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Punto_Poly.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Punto_Poly : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var puntoPolyDao: PuntoPolyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        puntoPolyDao = AppDatabase.getInstance(requireContext()).puntoPolyDao()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnadd = view.findViewById<Button>(R.id.btn_add_punto)
        var textlatitud = view.findViewById<EditText>(R.id.TextLatitud)
        var textlongitud = view.findViewById<EditText>(R.id.TextLongitud)

        btnadd.setOnClickListener {
            var fragment = Poly_config()

            val entity = PuntoPoly(
                id = null,
                latitud = textlatitud.text.toString().toDouble(),
                longitud = textlongitud.text.toString().toDouble(),

            )
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    puntoPolyDao.insert(entity)
                }
            }


            getParentFragmentManager().beginTransaction().replace(R.id.home_cont, fragment).commit()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__punto__poly, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_Punto_Poly.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Punto_Poly().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}