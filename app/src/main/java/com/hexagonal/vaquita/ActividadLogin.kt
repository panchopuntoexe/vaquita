package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ActividadLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //boton login
        var buttonEntrar = findViewById<Button>(R.id.botonLogin)
        buttonEntrar.setOnClickListener{
            val intencion = Intent(this,Home::class.java)
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.loremIpsum)
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        startActivity(intencion)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        return@OnClickListener
                    })
            builder.create()
            builder.show()
        }

        // boton registro
        var botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener{
            //Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
            val intencion = Intent(this,Home::class.java)
            //intencion.putExtra("Extra",extraerNombreApellido(textViewMail.text.toString()))
            startActivity(intencion)
        }



    }
}