package com.hexagonal.vaquita.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.ActividadNuevaCartera
import com.hexagonal.vaquita.databinding.FragmentHomeBinding
import com.hexagonal.vaquita.ui.settings.SettingsFragment
import android.R




class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cardView1 = binding.cardView1
        cardView1.setOnClickListener {
            val intencion =  Intent(this.activity, ActividadCompra::class.java)
            startActivity(intencion)
        }

        val perfil = binding.imageProfile
        perfil.setOnClickListener {
            val fragmentB = SettingsFragment()
            requireFragmentManager().beginTransaction()
                .replace(com.hexagonal.vaquita.R.id.nav_host_fragment_activity_home, fragmentB)
                .addToBackStack(this::class.java.getSimpleName())
                .commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}