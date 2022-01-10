package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadTarjetaDeCredito : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_tarjeta_de_credito)

        // boton  pago
        var botonTarjeta = findViewById<Button>(R.id.botonPagarTarjeta)
        botonTarjeta.setOnClickListener{
            val intencion = Intent(this,ActividadHome::class.java)
            startActivity(intencion)
        }
    }
}