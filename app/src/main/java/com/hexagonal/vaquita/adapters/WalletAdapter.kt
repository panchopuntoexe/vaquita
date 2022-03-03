package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Wallet

class WalletAdapter(
    private val context: Activity,
    private val wallets: List<Wallet>,
    private val onWalletListener: OnWalletListener
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
            textViewLugarWallet.text = wallets[position].lugar.toString()
            Glide.with(context).load(wallets[position].foto).into(imagenWallet)
        }
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

    class ViewHolder(val view: View, private val onWalletListener: OnWalletListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val textViewNombreWallet: TextView = view.findViewById(R.id.textViewNombreWallet)
        val textViewFechaWallet: TextView = view.findViewById(R.id.textViewFechaWallet)
        val textViewLugarWallet: TextView = view.findViewById(R.id.textViewLugarWallet)
        val imagenWallet: ImageView = view.findViewById(R.id.imageWallet)


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

