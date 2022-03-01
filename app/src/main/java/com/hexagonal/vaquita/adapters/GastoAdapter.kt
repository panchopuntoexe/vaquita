package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Gasto
import com.hexagonal.vaquita.entidades.Wallet

class GastoAdapter(
    private val context: Activity,
    private val gastos: List<Gasto>,
) :
    RecyclerView.Adapter<GastoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gasto_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            textViewNombreGasto.text = "${gastos[position].nombre}"
            textViewFechaGasto.text = "${gastos[position].fecha}"
            if(gastos[position].tipo.equals("gasto")){
                textViewValorGasto.text = "\$-${gastos[position].valor}"
                textViewValorGasto.setTextColor(Color.parseColor("#FF0000"))
            } else {
                textViewValorGasto.text = "\$+${gastos[position].valor}"
                textViewValorGasto.setTextColor(Color.parseColor("#008000"))
            }

        }
    }

    override fun getItemCount(): Int {
        return gastos.size
    }

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val textViewNombreGasto = view.findViewById<TextView>(R.id.textViewNombreGasto)
        val textViewFechaGasto = view.findViewById<TextView>(R.id.textViewFechaGasto)
        val textViewValorGasto = view.findViewById<TextView>(R.id.textViewValorGasto)
    }

}

