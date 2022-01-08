package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ActividadNuevaCartera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_nueva_cartera)

        // boton nueva wallet
        var botonNuevaWallet = findViewById<Button>(R.id.botonContinuar)
        botonNuevaWallet.setOnClickListener{
            val intencion = Intent(this,ActividadContactos::class.java)
            startActivity(intencion)
        }
    }
}