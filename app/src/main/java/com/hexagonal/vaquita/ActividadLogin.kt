package com.hexagonal.vaquita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ActividadLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var textEmail: EditText
    lateinit var textClave: EditText
    lateinit var buttonEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicialización de variables
        textEmail = findViewById(R.id.textEmailLogin)
        textClave = findViewById(R.id.textClave)

        //Inicializando Firebase Auth
        auth = Firebase.auth
        val email = textEmail.text.toString()
        val clave = textClave.text.toString()

        //leerDatos()

        //Botón Login
        buttonEntrar = findViewById(R.id.botonLogin)
        //buttonEntrar.setOnClickListener {
        //val intencion = Intent(this, ActividadHome::class.java)
        //val builder = AlertDialog.Builder(this)

        // Validaciones
        // Comprobación Email
/*            if (!email.validateEmail()) {
                textEmailLogin.setError("Correo Electrónico o Contraseña Incorrectos")
                return@setOnClickListener
                // Comprobación Longitud Contraseñas
                if (clave.length < 8) {
                    Toast.makeText(
                        this,
                        "Correo Electrónico o Contraseña Incorrectos",
                        Toast.LENGTH_LONG
                    ).show()
                    true
                    return@setOnClickListener
                }
            }*/
        //if (!ValidarDatosRequeridos())
        //return@setOnClickListener


        //AutenticarUsuario(email, clave)

        Authentication()

//            builder.setMessage(R.string.loremIpsum)
//                .setPositiveButton(R.string.ok,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        startActivity(intencion)
//                    })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        return@OnClickListener
//                    })
//            builder.create()
//            builder.show()
        //}

        // boton registro
        val botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener {
            //Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
            val intencion = Intent(this, ActividadRegistro::class.java)
            //intencion.putExtra("Extra",extraerNombreApellido(textViewMail.text.toString()))
            startActivity(intencion)
        }

    }

    // FUNCIONES

//    fun probarBase() {
//        val db = Firebase.firestore
//        val user = hashMapOf(
//            "Nombre" to "Ada",
//            "Apeyido" to "Lovelace",
//        )
//        val TAG = ""
//        db.collection("Usuarios")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }
//    }
//
//    fun leerDatos() {
//        val db = Firebase.firestore
//        val TAG = "Usuarios: "
//
//        db.collection("Usuarios").whereArrayContains("Wallets", "Sh2JdyEeLvu1LORENjNm")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document}")
//                }
////                Log.d(TAG, "${result}")
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//    }

    // Función Validar Email
    private fun ValidarDatosRequeridos(): Boolean {
        val email = textEmail.text.toString()
        val clave = textClave.text.toString()
        if (email.isEmpty()) {
            textEmail.setError("El email es obligatorio")
            textEmail.requestFocus()
            return false
        }
        if (clave.isEmpty()) {
            textClave.setError("La clave es obligatoria")
            textClave.requestFocus()
            return false
        }
        if (clave.length < 3) {
            textClave.setError("La clave debe tener al menos 3 caracteres")
            textClave.requestFocus()
            return false
        }
        return true
    }

/*    fun AutenticarUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("EXTRA_LOGIN", "signInWithEmail:success")
                    //Si pasa validación de datos requeridos, ir a pantalla home
                    val intencion = Intent(this, ActividadHome::class.java)
                    intencion.putExtra("EXTRA_LOGIN", auth.currentUser!!.email)
                    startActivity(intencion)
                    //finish()
                } else {
                    Log.w("EXTRA_LOGIN", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }*/

    private fun Authentication() {
        buttonEntrar.setOnClickListener {
            if (textEmail.text.isNotEmpty() && textClave.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        textEmail.text.toString(),
                        textClave.text.toString()
                    )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            showHome()
                        } else {
                            showAlert()
                        }
                    }
            } else {
                showAlertEmpty()
            }

        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al Usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Campos de registro vacíos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, ActividadHome::class.java)
        startActivity(homeIntent)
    }

}
