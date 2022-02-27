package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.entidades.Wallet.Companion.toWallet
import kotlinx.coroutines.tasks.await

class ParticipanteAdapter(
    private val context: Activity,
    private val usuarios: List<Usuario>,
    private val deuda: Double,
    val onWalletListener: GastoAdapter.OnWalletListener?
) :
    RecyclerView.Adapter<ParticipanteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallet_card, parent, false)
        return ViewHolder(view, onWalletListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            textViewNombreWallet.text = "${usuarios[position].nombre}"
            textViewFechaWallet.text = "${usuarios[position].correo}"
            textViewDeudaWallet.text = "\$-${deuda.toString()}"
        }
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class ViewHolder(val view: View, val onWalletListener: GastoAdapter.OnWalletListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val textViewNombreWallet = view.findViewById<TextView>(R.id.textViewNombreWallet)
        val textViewFechaWallet = view.findViewById<TextView>(R.id.textViewFechaWallet)
        val textViewDeudaWallet = view.findViewById<TextView>(R.id.textViewDeudaWallet)


        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onWalletListener?.onWalletClick(adapterPosition)
        }
    }

    interface OnWalletListener {
        fun onWalletClick(position: Int)
    }
}

