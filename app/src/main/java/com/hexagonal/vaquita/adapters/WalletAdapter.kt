package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Wallet

class WalletAdapter(
    private val context: Activity,
    private val wallets: List<Wallet>,
    private val deuda: Double,
    val onWalletListener: OnWalletListener
) :
    RecyclerView.Adapter<WalletAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallet_card, parent, false)
        return ViewHolder(view, onWalletListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            textViewNombreWallet.text = "${wallets[position].nombre}"
            textViewFechaWallet.text = "${wallets[position].fecha}"
            textViewDeudaWallet.text = "\$-${deuda.toString()}"
        }
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

    class ViewHolder(val view: View, val onWalletListener: OnWalletListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val textViewNombreWallet = view.findViewById<TextView>(R.id.textViewNombreWallet)
        val textViewFechaWallet = view.findViewById<TextView>(R.id.textViewFechaWallet)
        val textViewDeudaWallet = view.findViewById<TextView>(R.id.textViewDeudaWallet)


        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onWalletListener.onWalletClick(adapterPosition)
        }
    }

    interface OnWalletListener {
        fun onWalletClick(position: Int)
    }
}

