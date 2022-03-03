package com.hexagonal.vaquita.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.adapters.CarrusellAdapter
import com.hexagonal.vaquita.adapters.WalletAdapter
import com.hexagonal.vaquita.databinding.FragmentHomeBinding
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), WalletAdapter.OnWalletListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var perfil: ImageView
    private lateinit var textBienvenida: TextView
    private lateinit var wallets: List<Wallet>
    private lateinit var usuarioActual: Usuario

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.recycleViewWallets.addItemDecoration(
            DividerItemDecoration(
                binding.recycleViewWallets.context,
                DividerItemDecoration.VERTICAL
            )
        )

        perfil = binding.imageProfile

        homeViewModel.userActual?.observe(viewLifecycleOwner, Observer {
            usuarioActual = it
            textBienvenida = binding.textBienvenida
            textBienvenida.text = "${getString(R.string.bienvenido)} ${it.nombre}"
            Glide.with(this).load(it.foto).into(perfil)

            homeViewModel.viewModelScope.launch {
                val parser = JsonParser()
                val jsonElement = parser.parse(it.wallets.toString())
                val carteras: JsonObject = jsonElement.asJsonObject
                homeViewModel._userWallets.value = homeViewModel.getWallets(carteras.keySet())!!
            }

        })
        homeViewModel.wallets?.observe(viewLifecycleOwner, Observer {
            wallets = it
            binding.recycleViewWallets.adapter =
                WalletAdapter(this.requireActivity(), it, this)
            binding.recycleViewWallets.layoutManager =
                LinearLayoutManager(this.requireActivity())
            binding.recycleViewWallets.setHasFixedSize(true)

            binding.recyclerViewCarrusell.adapter =
                CarrusellAdapter(this.requireActivity(), it, this)
            binding.recyclerViewCarrusell.layoutManager =
                LinearLayoutManager(this.requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCarrusell.setHasFixedSize(true)
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWalletClick(position: Int) {
        val wallet = wallets[position]
        val intencion = Intent(this.requireActivity(), ActividadCompra::class.java)

        try {
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
        } catch (e: Exception) {
        }
    }
}