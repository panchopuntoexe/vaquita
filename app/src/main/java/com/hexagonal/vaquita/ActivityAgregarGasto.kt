package com.hexagonal.vaquita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.databinding.ActivityAgregarGastoBinding
import com.hexagonal.vaquita.datos.GASTOS
import com.hexagonal.vaquita.datos.WALLETS
import com.hexagonal.vaquita.entidades.Wallet

class ActivityAgregarGasto : AppCompatActivity() {

    private lateinit var binding : ActivityAgregarGastoBinding
    private lateinit var nombreGasto : EditText
    private lateinit var valorGasto : EditText
    private lateinit var fechaGasto : EditText
    private lateinit var imagenGasto : ImageView
    private lateinit var botonAgregar : Button
    private lateinit var botonCancelar : Button
    lateinit var wallet : Wallet
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_agregar_gasto)
        binding = ActivityAgregarGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recepción de wallet
        val intent = intent
        wallet = intent.getParcelableExtra<Wallet>("wallet")!!
        imagenGasto = binding.imageGasto
        nombreGasto = binding.textNombreGasto
        valorGasto = binding.textGastoValor
        fechaGasto = binding.textGastoDate

        Glide.with(this).load(wallet.foto).override(500,500).into(imagenGasto)

        // Botón Agregar
        botonAgregar = binding.buttonAgregarGasto
        botonAgregar.setOnClickListener {
            subirGasto()
        }

        // Botón Cancelar
        botonCancelar = binding.btnCancelar
        botonCancelar.setOnClickListener {
            super.onBackPressed()
        }
    }


    private fun subirGasto() {
        val nombre: String = nombreGasto.text.toString()
        val fecha: String = fechaGasto.text.toString()
        val gastos: String = valorGasto.text.toString()

        if (nombre.isNotEmpty() && fecha.isNotEmpty() && gastos.isNotEmpty()) {
            var retorno: Boolean
            val gastoNuevo = hashMapOf(
                "nombre" to nombre,
                "fecha" to fecha,
                "valor" to gastos.toDouble(),
                "tipo" to "gasto"
            )
            db.collection(GASTOS)
                .add(gastoNuevo)
                .addOnSuccessListener { documentReference ->
                    val gastoId = documentReference.id
                    db.collection(WALLETS)
                        .whereEqualTo("foto", wallet.foto)
                        .get()
                        .addOnSuccessListener { documentReference1 ->
                            val docId = documentReference1.first().id
                            val mapaGastos =documentReference1.first().get("gastos") as MutableMap<String, Boolean>
                            mapaGastos[gastoId] = true
                            db.collection(WALLETS).document(docId)
                                .update("gastos" ,mapaGastos)
                            val intencion = Intent(this, ActividadHome::class.java)
                            startActivity(intencion)
                        }
                    retorno = true
                }
                .addOnFailureListener {
                    retorno = false
                }
        } else {
            Toast.makeText(this, "No dejar campos vacíos", Toast.LENGTH_LONG).show()
        }

    }


}