package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class ActividadInicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_inicio)

        // logout
        var fotoDePerfil = findViewById<ImageView>(R.id.imageProfile)
        fotoDePerfil.setOnClickListener{
            //builder del diálogo
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.preguntaLogout)
                .setPositiveButton(R.string.logout,
                    DialogInterface.OnClickListener { dialog, id ->
                        //envío a inicio
                        val intencion = Intent(this,ActividadLogin::class.java)
                        startActivity(intencion)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        return@OnClickListener
                    })
            builder.create()
            builder.show()
        }
    }
}