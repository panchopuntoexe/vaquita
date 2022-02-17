package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityAgregarGasto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_gasto)

        // boton  agregar
        var botonMetodoDePago = findViewById<Button>(R.id.button4)
        botonMetodoDePago.setOnClickListener {
            val intencion = Intent(this, ActividadInicio::class.java)
            startActivity(intencion)
        }
    }
}