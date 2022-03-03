package com.hexagonal.vaquita

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.hexagonal.vaquita.entidades.Wallet

class ActividadMetodoDePago : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_metodo_de_pago)

        val intent = intent
        val deuda = intent.getDoubleExtra("deuda", 0.0)
        val wallet = intent.getParcelableExtra<Wallet>("wallet")

        // Botón  Efectivo
        val botonEfectivo: CardView = findViewById(R.id.btnEfectivo)
        botonEfectivo.setOnClickListener{
            val intencion = Intent(this,ActividadEfectivo::class.java)
            intencion.putExtra("deuda", deuda)
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
        }

        // Botón  Paypal
        val botonPaypal: CardView = findViewById(R.id.btnPaypal)
        botonPaypal.setOnClickListener{
            val intencion = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/ec/home"))
            startActivity(intencion)
        }

        // Botón Tarjeta
        val botonTarjeta: CardView = findViewById(R.id.btnTarjetaCredito)
        botonTarjeta.setOnClickListener{
            val intencion = Intent(this,ActividadTarjetaDeCredito::class.java)
            intencion.putExtra("deuda", deuda)
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
        }



        // Botón  Cancel
        val botonCancelar = findViewById<Button>(R.id.botonCancelar)
        botonCancelar.setOnClickListener{
            super.onBackPressed()
        }
    }
}