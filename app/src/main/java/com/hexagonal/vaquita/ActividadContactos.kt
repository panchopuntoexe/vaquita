package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadContactos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_contactos)
        // boton guardar Contactos
        var botonSalvar = findViewById<Button>(R.id.botonPagar)
        botonSalvar.setOnClickListener{
            val intencion = Intent(this,Home::class.java)
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.exitoWallet)
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        startActivity(intencion)
                    })
            builder.create()
            builder.show()
        }
    }
}