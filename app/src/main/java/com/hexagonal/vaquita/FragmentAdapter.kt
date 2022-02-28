package com.hexagonal.vaquita

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hexagonal.vaquita.adapters.GastoAdapter

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val onWalletListener: GastoAdapter.OnWalletListener,
    val usuarios: Map<String, Boolean>?,
    val gastos: Map<String, Boolean>?,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GastosFragment(gastos, onWalletListener)
            }
            1 -> {
                ParticipantesFragment(usuarios, onWalletListener)
            }
            else -> {
                Fragment()
            }
        }
    }

}