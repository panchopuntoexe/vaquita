package com.hexagonal.vaquita

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hexagonal.vaquita.adapters.GastoAdapter
import com.hexagonal.vaquita.entidades.Usuario

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val onWalletListener: GastoAdapter.OnWalletListener,
    val usuarios: ArrayList<Usuario>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GastosFragment()
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