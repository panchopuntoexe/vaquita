package com.hexagonal.vaquita.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hexagonal.vaquita.GastosFragment
import com.hexagonal.vaquita.ParticipantesFragment
import com.hexagonal.vaquita.entidades.Wallet

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val wallet : Wallet?
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GastosFragment(wallet)
            }
            1 -> {
                ParticipantesFragment(wallet?.users)
            }
            else -> {
                Fragment()
            }
        }
    }

}