package com.hexagonal.vaquita.ui.pay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.adapters.WalletAdapter
import com.hexagonal.vaquita.databinding.FragmentPayBinding
import com.hexagonal.vaquita.entidades.Wallet
import kotlinx.coroutines.launch

class PayFragment : Fragment(), WalletAdapter.OnWalletListener {

    private lateinit var payViewModel: PayViewModel
    private var _binding: FragmentPayBinding? = null
    private lateinit var wallets: List<Wallet>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        payViewModel =
            ViewModelProvider(this)[PayViewModel::class.java]

        _binding = FragmentPayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        payViewModel.userActual?.observe(viewLifecycleOwner, Observer {
            payViewModel.viewModelScope.launch {
                val parser = JsonParser()
                val jsonElement = parser.parse(it.wallets.toString())
                val carteras: JsonObject = jsonElement.asJsonObject
                payViewModel._userWallets.value = payViewModel.getWallets(carteras.keySet())!!
            }

        })

        payViewModel.wallets?.observe(viewLifecycleOwner, Observer {
            wallets = it
            binding.recyclerViewPay.adapter =
                WalletAdapter(this.requireActivity(), it, this)
            binding.recyclerViewPay.layoutManager =
                LinearLayoutManager(this.requireActivity())
            binding.recyclerViewPay.setHasFixedSize(true)
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