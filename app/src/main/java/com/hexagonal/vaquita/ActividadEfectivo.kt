package com.hexagonal.vaquita

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import java.text.SimpleDateFormat
import java.util.*

class ActividadEfectivo : AppCompatActivity() {

    private lateinit var textCantidadEfectivo: TextView
    private lateinit var textPagoRealizarse: EditText
    lateinit var nombre  : String
    private lateinit var idUser : String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_efectivo)

        val intent = intent
        val deuda = intent.getDoubleExtra("deuda", 0.0)
        val wallet = intent.getParcelableExtra<Wallet>("wallet") as Wallet

        textCantidadEfectivo = findViewById(R.id.textCantidadEfectivo)
        textPagoRealizarse = findViewById(R.id.textPagoRealizarse)

        textCantidadEfectivo.text = deuda.toString()

        getUser()

        // Botón Pago
        val botonEfectivo = findViewById<Button>(R.id.botonPagarEfectivo)
        botonEfectivo.setOnClickListener {
            val valorAPagar = textPagoRealizarse.text.toString().toDouble()
            val subirPago = GestionadorDeSubida()
            val pago = Pago(
                "Pago de: $nombre",
                valorAPagar,
                idUser,
                SimpleDateFormat("dd/M/yyyy").format(Date()),
                "pago"
            )

            if (valorAPagar > 0) {
                subirPago.subirPago(this, pago, wallet)
            } else {
                Toast.makeText(this, "Valor ingresado inválido", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Cancelar
        val botonCancelar = findViewById<Button>(R.id.botonCancelarEfectivo)
        botonCancelar.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun getUser() {
        val userEmail: String? = Firebase.auth.currentUser?.email
        val db = Firebase.firestore
        var userfb : Usuario
        db.collection("Usuarios")
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                userfb = document.toObject(Usuario::class.java)
                nombre = userfb.nombre.toString()
                idUser = document.id
            }
    }
}