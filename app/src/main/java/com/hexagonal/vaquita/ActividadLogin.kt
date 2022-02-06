package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ActividadLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leerDatos()

        //boton login
        var buttonEntrar = findViewById<Button>(R.id.botonLogin)
        buttonEntrar.setOnClickListener{
            val intencion = Intent(this,ActividadHome::class.java)
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
            val intencion = Intent(this,ActividadRegistro::class.java)
            //intencion.putExtra("Extra",extraerNombreApellido(textViewMail.text.toString()))
            startActivity(intencion)
        }

    }

    fun probarBase (){
        val db = Firebase.firestore
        val user = hashMapOf(
            "Nombre" to "Ada",
            "Apeyido" to "Lovelace",
        )
        val TAG = ""
        db.collection("Usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    fun leerDatos(){
        val db = Firebase.firestore
        val TAG = "Usuarios: "

        db.collection("Usuarios").whereArrayContains("Wallets","Sh2JdyEeLvu1LORENjNm")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document}")
                }
//                Log.d(TAG, "${result}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}