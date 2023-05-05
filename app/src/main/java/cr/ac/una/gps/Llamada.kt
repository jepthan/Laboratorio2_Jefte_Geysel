package cr.ac.una.gps

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass.
 * Use the [Llamada.newInstance] factory method to
 * create an instance of this fragment.
 */
class Llamada : Fragment() {


    lateinit var botonGuardar: Button
    lateinit var numero: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_llamada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var btn = view.findViewById<Button>(R.id.botonguardar)
        btn.setOnClickListener {
            makePhoneCall2()
        }
    }

    fun makePhoneCall2(){
        val intent = Intent(Intent.ACTION_CALL)
        val phoneNumber = view?.findViewById<EditText>(R.id.numero)?.text.toString()
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }


    fun makePhoneCall2(view: View) {

        val permission = android.Manifest.permission.CALL_PHONE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 1)
        } else {
            makePhoneCall2()
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (1) {
            requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall2()
                } else {
                    // El usuario no concedió el permiso
                }
            }
        }
    }



}