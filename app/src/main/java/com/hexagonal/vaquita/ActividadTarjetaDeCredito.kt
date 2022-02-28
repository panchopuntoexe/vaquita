package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadTarjetaDeCredito : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_tarjeta_de_credito)

        // Botón Pago
        var botonTarjeta = findViewById<Button>(R.id.botonPagarTarjeta)
        botonTarjeta.setOnClickListener{
            val intencion = Intent(this,ActividadHome::class.java)
            startActivity(intencion)
        }

        // Botón Cancelar
        var botonCancelar = findViewById<Button>(R.id.botonCancelarTarjeta)
        botonCancelar.setOnClickListener{
            val intencion = Intent(this,ActividadCompra::class.java)
            startActivity(intencion)
        }
    }
}