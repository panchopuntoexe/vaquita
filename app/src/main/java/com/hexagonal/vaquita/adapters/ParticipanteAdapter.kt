package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Usuario
import kotlin.math.pow
import kotlin.math.roundToInt

class ParticipanteAdapter(
    private val context: Activity,
    private val usuarios: List<Usuario>,
    private val deuda: Double,
    private val pagosUsuarios: MutableMap<String?, Double>
) :
    RecyclerView.Adapter<ParticipanteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.participantes_card, parent, false)

        Log.d("UsuariosWW", usuarios.toString())
        return ViewHolder(view)
    }

    fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pagoUser: TextView = context.findViewById(R.id.pagoUser)
        with(holder) {
            Glide.with(context).load(usuarios[position].foto).into(imageParticipante!!)
            textViewNombreParticipante.text = "${usuarios[position].nombre}"
            if (pagosUsuarios.size > 0) {
                var pago = 0.0
                for (p in pagosUsuarios) {
                    if (usuarios[position].correo == p.key) {
                        pago = p.value
                        break
                    }
                }
                val deudaC = (deuda - pago).roundTo(2)
                textViewDeudaParticipante.text = "\$ ${deudaC.toString()}"
                pagoUser.text = deudaC.toString()
            } else {
                textViewDeudaParticipante.text = "\$ ${deuda.toString()}"
                pagoUser.text = deuda.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val textViewNombreParticipante =
            view.findViewById<TextView>(R.id.textViewNombreParticipante)
        val textViewDeudaParticipante = view.findViewById<TextView>(R.id.textViewDeudaParticipante)
        val imageParticipante = view.findViewById<ImageView>(R.id.imageParticipante)

    }
}

