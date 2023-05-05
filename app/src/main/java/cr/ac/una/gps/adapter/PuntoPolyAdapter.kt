package cr.ac.una.gps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.una.gps.entity.PuntoPoly
import cr.ac.una.gps.R

class PuntoPolyAdapter(context: Context, PuntosPoly: List<PuntoPoly>) :
    ArrayAdapter<PuntoPoly>(context, 0, PuntosPoly) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val puntoPoly = getItem(position)

        val numeroPolyView = view!!.findViewById<TextView>(R.id.numero_poly)
        val latitudTextView = view.findViewById<TextView>(R.id.latitud)
        val longitudTextView = view.findViewById<TextView>(R.id.longitud)

        numeroPolyView.text = puntoPoly!!.id.toString()
        latitudTextView.text = puntoPoly.latitud.toString()
        longitudTextView.text = puntoPoly.longitud.toString()

        return view
    }
}