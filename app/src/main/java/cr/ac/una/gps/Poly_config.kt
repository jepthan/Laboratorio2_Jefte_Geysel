package cr.ac.una.gps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import cr.ac.una.gps.adapter.PuntoPolyAdapter
import cr.ac.una.gps.dao.PuntoPolyDao
import cr.ac.una.gps.db.AppDatabase
import cr.ac.una.gps.entity.PuntoPoly
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Poly_config.newInstance] factory method to
 * create an instance of this fragment.
 */
class Poly_config : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poly_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listPoly)
        var puntos: List<PuntoPoly>

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                puntos = puntoPolyDao.getAll() as List<PuntoPoly>
                withContext(Dispatchers.Main){
                    val adapter = PuntoPolyAdapter(requireContext(), puntos)
                    listView.adapter = adapter
                }

            }

        }

        var btn_add = view.findViewById<Button>(R.id.Add_punto)



        btn_add.setOnClickListener { view->
            var fragment = Add_Punto_Poly()

            getParentFragmentManager().beginTransaction().replace(R.id.home_cont, fragment).commit()
        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Poly_config.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Poly_config().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}