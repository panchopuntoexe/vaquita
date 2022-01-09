package com.hexagonal.vaquita.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.hexagonal.vaquita.ActividadLogin
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        var btn_signout = binding.btnsignout
        btn_signout.setOnClickListener {
            //builder del diálogo
            val builder = AlertDialog.Builder(this.activity)
            builder.setMessage(R.string.preguntaLogout)
                .setPositiveButton(R.string.logout,
                    DialogInterface.OnClickListener { dialog, id ->
                        //envío a inicio
                        val intencion = Intent(this.activity, ActividadLogin::class.java)
                        startActivity(intencion)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        return@OnClickListener
                    })
            builder.create()
            builder.show()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}