package com.hexagonal.vaquita

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.gms.common.util.HttpUtils.parse
import com.hexagonal.vaquita.entidades.Wallet
import java.net.HttpCookie.parse
import java.net.URI
import java.util.logging.Level.parse

class ActividadMetodoDePago : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_metodo_de_pago)

        val intent = getIntent()
        val deuda = intent.getDoubleExtra("deuda", 0.0)
        val wallet = intent.getParcelableExtra<Wallet>("wallet")

        // Botón  Efectivo
        var botonEfectivo: CardView = findViewById(R.id.btnEfectivo)
        botonEfectivo.setOnClickListener{
            val intencion = Intent(this,ActividadEfectivo::class.java)
            intencion.putExtra("deuda", deuda)
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
        }

        // Botón  Paypal
        var botonPaypal: CardView = findViewById(R.id.btnPaypal)
        botonPaypal.setOnClickListener{
            val intencion = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/ec/home"))
            startActivity(intencion)
        }

        // Botón Tarjeta
        var botonTarjeta: CardView = findViewById(R.id.btnTarjetaCredito)
        botonTarjeta.setOnClickListener{
            val intencion = Intent(this,ActividadTarjetaDeCredito::class.java)
            intencion.putExtra("deuda", deuda)
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
        }



        // Botón  Cancel
        var botonCancelar = findViewById<Button>(R.id.botonCancelar)
        botonCancelar.setOnClickListener{
            super.onBackPressed()
        }
    }
}