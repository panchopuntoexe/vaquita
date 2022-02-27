package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.datos.FileExternalManager
import com.hexagonal.vaquita.datos.FileHandler



class ActividadLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var textEmail: EditText
    lateinit var textClave: EditText
    lateinit var buttonEntrar: Button
    lateinit var checkBoxRecordarme: CheckBox
    lateinit var manejadorArchivo: FileHandler
    lateinit var botonRecuperar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicialización de variables
        textEmail = findViewById(R.id.textEmailLogin)
        textClave = findViewById(R.id.textClave)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        buttonEntrar = findViewById(R.id.botonLogin)
        manejadorArchivo = FileExternalManager(this)
        botonRecuperar = findViewById(R.id.buttonRecuperar)

        //Inicializando Firebase Auth
        auth = Firebase.auth
        val email = textEmail.text.toString()
        textEmail.setText("francisco.garcia@epn.edu.ec")
        val clave = textClave.text.toString()

        // Leyendo Datos de Preferencias
        LeerDatosDePreferencias()

        //Botón Login
        Authentication()

        // Botón Registro
        val botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener {
            //Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
            val intencion = Intent(this, ActividadRegistro::class.java)
            //intencion.putExtra("Extra",extraerNombreApellido(textViewMail.text.toString()))
            startActivity(intencion)
        }

        //Boton Recuperar
        botonRecuperar.setOnClickListener{
            val intencion = Intent(this, ActividadRecuperar::class.java)
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

    private fun Authentication() {
        buttonEntrar.setOnClickListener {
            if (textEmail.text.isNotEmpty() && textClave.text.isNotEmpty()) {
                GuardarDatosEnPreferencias()
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

    private fun GuardarDatosEnPreferencias() {
        val email = textEmail.text.toString()
        val clave = textClave.text.toString()
        val listadoAGrabar: Pair<String, String>
        if (checkBoxRecordarme.isChecked) {
            listadoAGrabar = email to clave
        } else {
            listadoAGrabar = "" to ""
        }
        manejadorArchivo.SaveInformation(listadoAGrabar)
    }

    private fun LeerDatosDePreferencias() {
        val listadoLeido = manejadorArchivo.ReadInformation()
        if (listadoLeido.first != null) {
            checkBoxRecordarme.isChecked = true
        }
        textEmail.setText(listadoLeido.first)
        textClave.setText(listadoLeido.second)
    }
}
