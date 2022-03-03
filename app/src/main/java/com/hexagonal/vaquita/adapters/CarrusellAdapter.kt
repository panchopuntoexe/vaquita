package com.hexagonal.vaquita.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.ui.home.HomeFragment

class CarrusellAdapter(
    private val context: Activity,
    private val wallets: List<Wallet>,
    private val onWalletListener: HomeFragment
    ) :
    RecyclerView.Adapter<CarrusellAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.carrusell_card, parent, false)
            return ViewHolder(view, onWalletListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                Glide.with(context).load(wallets[position].foto).into(imagenWallet)
            }
        }

        override fun getItemCount(): Int {
            return wallets.size
        }

        class ViewHolder(val view: View, private val onWalletListener: HomeFragment) :
            RecyclerView.ViewHolder(view), View.OnClickListener {
            val imagenWallet: ImageView = view.findViewById(R.id.imagenCarrusell)

            init {
                view.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                onWalletListener.onWalletClick(adapterPosition)
            }
        }
}