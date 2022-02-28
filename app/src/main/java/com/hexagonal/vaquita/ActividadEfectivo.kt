package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadEfectivo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_efectivo)

        // Botón Pago
        var botonEfectivo = findViewById<Button>(R.id.botonPagarEfectivo)
        botonEfectivo.setOnClickListener{
            val intencion = Intent(this,ActividadHome::class.java)
            startActivity(intencion)
        }

        // Botón Cancelar
        var botonCancelar = findViewById<Button>(R.id.botonCancelarEfectivo)
        botonCancelar.setOnClickListener{
            val intencion = Intent(this,ActividadCompra::class.java)
            startActivity(intencion)
        }
    }
}