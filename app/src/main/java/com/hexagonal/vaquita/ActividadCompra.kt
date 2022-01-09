package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadCompra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_compra)

        // boton  método de pago
        var botonMetodoDePago = findViewById<Button>(R.id.botonPagar)
        botonMetodoDePago.setOnClickListener{
            val intencion = Intent(this,ActividadMetodoDePago::class.java)
            startActivity(intencion)
        }
    }
}