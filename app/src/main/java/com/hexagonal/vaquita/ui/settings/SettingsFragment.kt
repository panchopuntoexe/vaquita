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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.ActividadLogin
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.databinding.FragmentSettingsBinding
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.entidades.Usuario


class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var usuario: TextView
    private lateinit var username: TextView
    private lateinit var correo: TextView
    private lateinit var password: TextView
    private lateinit var profilePic : ImageView


    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        inflater.inflate(R.layout.fragment_settings, container, false)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val btnsignout = binding.btnsignout
        btnsignout.setOnClickListener {
            //builder del diálogo
            val builder = AlertDialog.Builder(this.activity)
            builder.setMessage(R.string.preguntaLogout)
                .setPositiveButton(R.string.logout
                ) { _, _ ->
                    //envío a inicio
                    Firebase.auth.signOut()
                    val intencion = Intent(this.activity, ActividadLogin::class.java)
                    startActivity(intencion)
                }
                .setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { _, _ ->
                        return@OnClickListener
                    })
            builder.create()
            builder.show()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuario = view.findViewById(R.id.textView10)
        username = view.findViewById(R.id.textView)
        correo = view.findViewById(R.id.textViewEditEmail)
        password = view.findViewById(R.id.textViewEditPassword)
        profilePic = view.findViewById(R.id.imageView)

        getUser()
    }
    private fun getUser() {
        val db = Firebase.firestore
        var userfb : Usuario
        db.collection(USUARIOS)
            .whereEqualTo("correo", Firebase.auth.currentUser!!.email)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                userfb = document.toObject(Usuario::class.java)
                usuario.text = userfb.nombre.toString()
                username.text = userfb.username.toString()
                correo.text = userfb.correo.toString()
                Glide.with(this).load(userfb.foto).into(profilePic)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}