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
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import com.hexagonal.vaquita.validador.Validador
import java.text.SimpleDateFormat
import java.util.*

class ActividadTarjetaDeCredito : AppCompatActivity() {

    private val validador: Validador = Validador()
    private lateinit var textCantidad: TextView
    private lateinit var textValorAPagar: EditText
    private lateinit var textNombreDeTarjeta: EditText
    private lateinit var textNumeroTarjeta: EditText
    private lateinit var textFechaDeExp: EditText
    private lateinit var textCcv: EditText
    private lateinit var textNumeroPostal: EditText
    lateinit var nombre: String
    private lateinit var idUser: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_tarjeta_de_credito)

        val intent = intent
        val deuda = intent.getDoubleExtra("deuda", 0.0)
        val wallet = intent.getParcelableExtra<Wallet>("wallet") as Wallet

        textValorAPagar = findViewById(R.id.textValorAPagar)
        textCantidad = findViewById(R.id.textCantidad)
        textNombreDeTarjeta = findViewById(R.id.textNombreDeTarjeta)
        textNumeroTarjeta = findViewById(R.id.textNumeroTarjeta)
        textFechaDeExp = findViewById(R.id.textFechaDeExp)
        textCcv = findViewById(R.id.textCcv)
        textNumeroPostal = findViewById(R.id.textNumeroPostal)

        textCantidad.text = deuda.toString()


        getUser()

        // Botón Pago
        val botonTarjeta = findViewById<Button>(R.id.botonPagarTarjeta)
        botonTarjeta.setOnClickListener {
            if (textValorAPagar.text.isNotEmpty()) {
                val valorAPagar = textValorAPagar.text.toString().toDouble()
                val valorADeuda = textCantidad.text.toString().toDouble()
                val subirPago = GestionadorDeSubida()
                val pago = Pago(
                    "Pago de: $nombre",
                    valorAPagar,
                    idUser,
                    SimpleDateFormat("dd/M/yyyy").format(Date()),
                    "pago"
                )
                if (valorAPagar > 0 && valorAPagar != null && valorADeuda >= valorAPagar) {
                    if (validarTarjeta()) {
                        subirPago.subirPago(this, pago, wallet)
                    } else {
                        Toast.makeText(this, "Datos de la tarjeta inválidos", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Valor ingresado inválido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Valor ingresado inválido", Toast.LENGTH_SHORT).show()
            }

            // Botón Cancelar
            val botonCancelar = findViewById<Button>(R.id.botonCancelarTarjeta)
            botonCancelar.setOnClickListener {
                super.onBackPressed()
            }
        }
    }

    private fun getUser() {
        val userEmail: String? = Firebase.auth.currentUser?.email
        val db = Firebase.firestore
        var userfb: Usuario
        db.collection(USUARIOS)
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                userfb = document.toObject(Usuario::class.java)
                nombre = userfb.nombre.toString()
                idUser = document.id
            }
    }

    // Validador Tarjeta de Crédito
    private fun validarTarjeta(): Boolean {
        var retorno = false
        if (validador.esLongitudMenor(textNombreDeTarjeta.text.toString(), 30)) {
            if (validador.esLongitudIgual(textNumeroTarjeta.text.toString(), 12)) {
                if (validador.esLongitudIgual(textFechaDeExp.text.toString(), 5)) {
                    if (validador.esLongitudIgual(textCcv.text.toString(), 3)) {
                        if (validador.esLongitudIgual(textNumeroPostal.text.toString(), 6)) {
                            retorno = true
                        }
                    } else {
                        retorno = false
                    }
                } else {
                    retorno = false
                }
            } else {
                retorno = false
            }
        } else {
            retorno = false
        }
        return retorno
    }

}