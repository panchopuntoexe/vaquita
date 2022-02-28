package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.databinding.ActivityAgregarGastoBinding
import com.hexagonal.vaquita.databinding.ActivityHomeBinding
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet

class ActivityAgregarGasto : AppCompatActivity() {

    private lateinit var binding : ActivityAgregarGastoBinding;
    lateinit var nombreGasto : EditText;
    lateinit var valorGasto : EditText;
    lateinit var fechaGasto : EditText;
    lateinit var imagenGasto : ImageView;
    lateinit var botonAgregar : Button;
    lateinit var botonCancelar : Button;
    lateinit var wallet : Wallet;
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_gasto)

        // Recepción de wallet
        val intent = getIntent()
        wallet = intent.getParcelableExtra<Wallet>("wallet") as Wallet

        binding = ActivityAgregarGastoBinding.inflate(layoutInflater)

        imagenGasto = binding.imageGasto;
        nombreGasto = binding.textNombreGasto;
        valorGasto = binding.textGastoValor;
        fechaGasto = binding.textGastoDate;

        Glide.with(this).load(wallet?.foto).into(imagenGasto)

        // Botón Agregar
        botonAgregar = binding.buttonAgregarGasto
        botonAgregar.setOnClickListener {
            val intencion = Intent(this, ActividadCompra::class.java)
            startActivity(intencion)
        }

        // Botón Cancelar
        botonCancelar = binding.btnCancelar
        botonCancelar.setOnClickListener {
            val intencion = Intent(this, ActividadCompra::class.java)
            startActivity(intencion)
        }
    }

    /*fun subirGasto() {
        var nombre: String = nombreGasto.text.toString()
        var fecha: String = fechaGasto.text.toString()
        var gastos: String = valorGasto.text.toString()

        if (nombre.isNotEmpty() && fecha.isNotEmpty() && gastos.isNotEmpty()) {
            var retorno: Boolean = true
            var mapaVacio:Map<String,Boolean> = emptyMap<String,Boolean>()
            val gastoNuevo = hashMapOf(
                "nombre" to nombreGasto,
                "fecha" to valorGasto,
                "gastos" to fechaGasto,
            )
            val TAG = "MENSAJE"
            db.collection("Gastos")
                .add(gastoNuevo)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Gasto added with ID: ${documentReference.id}")
                    val gastoId = documentReference.id;
                    var userfb : Usuario
                    db.collection("Wallets")
                        .whereEqualTo("foto", wallet.foto)
                        .add(gastoNuevo)

                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Gasto added with ID: ${documentReference.id}")
                            val gastoId = documentReference.id;
                            var userfb : Usuario
                            retorno = true
                        }
                    retorno = true
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    retorno = false
                }
            return retorno
        } else {
            Toast.makeText(this, "No dejar campos vacíos", Toast.LENGTH_LONG).show()
        }


    }*/


}