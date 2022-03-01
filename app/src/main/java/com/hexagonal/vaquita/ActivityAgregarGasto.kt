package com.hexagonal.vaquita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.databinding.ActivityAgregarGastoBinding
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
        //setContentView(R.layout.activity_agregar_gasto)
        binding = ActivityAgregarGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recepción de wallet
        val intent = getIntent()
        wallet = intent.getParcelableExtra<Wallet>("wallet")!!

        Log.d("Hola0", wallet.toString())

        imagenGasto = binding.imageGasto;
        nombreGasto = binding.textNombreGasto;
        valorGasto = binding.textGastoValor;
        fechaGasto = binding.textGastoDate;

        Glide.with(this).load(wallet.foto).into(imagenGasto)

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


    fun subirGasto() {
        val nombre: String = nombreGasto.text.toString()
        val fecha: String = fechaGasto.text.toString()
        val gastos: String = valorGasto.text.toString()

        Log.d("data", nombre + fecha + gastos)

        if (nombre.isNotEmpty() && fecha.isNotEmpty() && gastos.isNotEmpty()) {
            var retorno: Boolean = true
            val gastoNuevo = hashMapOf(
                "nombre" to nombre,
                "fecha" to fecha,
                "valor" to gastos.toDouble(),
                "tipo" to "gasto"
            )
            val TAG = "MENSAJE"
            db.collection("Gastos")
                .add(gastoNuevo)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Gasto added with ID: ${documentReference.id}")
                    val gastoId = documentReference.id;
                    db.collection("Wallets")
                        .whereEqualTo("foto", wallet.foto)
                        .get()
                        .addOnSuccessListener { documentReference1 ->
                            val docId = documentReference1.first().id
                            val mapaGastos =documentReference1.first().get("gastos") as MutableMap<String, Boolean>
                            mapaGastos.put(gastoId, true)
                            db.collection("Wallets").document(docId)
                                .update("gastos" ,mapaGastos)
                            //Log.d("Gasto agregado", mapaGastos.toString())
                            val intencion = Intent(this, ActividadHome::class.java)
                            startActivity(intencion)
                        }
                    retorno = true
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    retorno = false
                }
        } else {
            Toast.makeText(this, "No dejar campos vacíos", Toast.LENGTH_LONG).show()
        }

    }


}