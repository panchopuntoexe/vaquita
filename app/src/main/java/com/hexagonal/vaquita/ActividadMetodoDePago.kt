package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView

class ActividadMetodoDePago : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_metodo_de_pago)

        // boton  cancel
        var botonCancelar = findViewById<Button>(R.id.botonCancelar)
        botonCancelar.setOnClickListener{
            val intencion = Intent(this,ActividadCompra::class.java)
            startActivity(intencion)
        }

        // boton  tarjeta
        var botonTarjeta: CardView = findViewById(R.id.btnTarjetaCredito)
        botonTarjeta.setOnClickListener{
            val intencion = Intent(this,ActividadTarjetaDeCredito::class.java)
            startActivity(intencion)
        }
    }
}