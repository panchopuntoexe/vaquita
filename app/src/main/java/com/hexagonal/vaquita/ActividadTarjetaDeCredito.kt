package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import java.text.SimpleDateFormat
import java.util.*

class ActividadTarjetaDeCredito : AppCompatActivity() {

    lateinit var textCantidad : TextView;
    lateinit var textValorAPagar : TextView;
    lateinit var nombre  : String
    lateinit var idUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_tarjeta_de_credito)

        val intent = getIntent()
        val deuda = intent.getDoubleExtra("deuda", 0.0)
        val wallet = intent.getParcelableExtra<Wallet>("wallet") as Wallet

        textValorAPagar = findViewById(R.id.textValorAPagar)
        textCantidad = findViewById(R.id.textCantidad)

        textCantidad.setText("$ " + deuda.toString())


        getUser()

        // Botón Pago
        var botonTarjeta = findViewById<Button>(R.id.botonPagarTarjeta)
        botonTarjeta.setOnClickListener{
            val valorAPagar = textValorAPagar.text.toString().toDouble()
            var subirPago : GestionadorDeSubida = GestionadorDeSubida()
            var pago : Pago = Pago(
                "Pago de: " + nombre,
                valorAPagar,
                idUser,
                SimpleDateFormat("dd/M/yyyy").format(Date()),
                "pago"
            )

            if (valorAPagar > 0 && valorAPagar != null) {
                if (subirPago.subirPago(pago, wallet)){
                    val intencion = Intent(this, ActividadHome::class.java)
                    startActivity(intencion)
                }else {
                    Toast.makeText(this, "Error al realizar el Pago", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Valor ingresado inválido", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Cancelar
        var botonCancelar = findViewById<Button>(R.id.botonCancelarTarjeta)
        botonCancelar.setOnClickListener{
            super.onBackPressed()
        }
    }

    fun getUser() {
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