package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActividadRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_registro)

        // boton registro
        var botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener{
            //builder del diálogo
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.exitoRegistro)
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        //envío a inicio
                        val intencion = Intent(this,ActividadHome::class.java)
                        startActivity(intencion)
                    })/*
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })*/
            builder.create()
            builder.show()
        }
    }
}

