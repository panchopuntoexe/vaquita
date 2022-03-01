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
import com.google.firebase.firestore.FieldPath
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.entidades.Wallet.Companion.toWallet
import kotlinx.coroutines.tasks.await

class ParticipanteAdapter(
    private val context: Activity,
    private val usuarios: List<Usuario>,
    private val deuda: Double
) :
    RecyclerView.Adapter<ParticipanteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.participantes_card, parent, false)

        Log.d("UsuariosWW", usuarios.toString())
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            Glide.with(context).load(usuarios[position].foto).into(imageParticipante!!)
            textViewNombreParticipante.text = "${usuarios[position].nombre}"
            textViewDeudaParticipante.text = "\$${deuda.toString()}"
        }
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view){
        val textViewNombreParticipante = view.findViewById<TextView>(R.id.textViewNombreParticipante)
        val textViewDeudaParticipante = view.findViewById<TextView>(R.id.textViewDeudaParticipante)
        val imageParticipante = view.findViewById<ImageView>(R.id.imageParticipante)
    }

}

