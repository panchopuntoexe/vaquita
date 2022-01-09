package com.hexagonal.vaquita.ui.pay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.databinding.FragmentPayBinding

class PayFragment : Fragment() {

    private lateinit var payViewModel: PayViewModel
    private var _binding: FragmentPayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        payViewModel =
            ViewModelProvider(this).get(PayViewModel::class.java)

        _binding = FragmentPayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cardView1 = binding.cardView1
        cardView1.setOnClickListener {
            val intencion =  Intent(this.activity, ActividadCompra::class.java)
            startActivity(intencion)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}