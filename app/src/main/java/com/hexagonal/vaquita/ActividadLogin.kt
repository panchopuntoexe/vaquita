package com.hexagonal.vaquita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.datos.FileExternalManager
import com.hexagonal.vaquita.datos.FileHandler


class ActividadLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var textEmail: EditText
    private lateinit var textClave: EditText
    private lateinit var buttonEntrar: Button
    private lateinit var checkBoxRecordarme: CheckBox
    private lateinit var manejadorArchivo: FileHandler
    private lateinit var botonRecuperar: Button

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
        getUserFirebase()

        // Leyendo Datos de Preferencias
        leerDatosDePreferencias()

        //Botón Login
        authentication()

        // Botón Registro
        val botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener {
            val intencion = Intent(this, ActividadRegistro::class.java)
            startActivity(intencion)
        }

        //Boton Recuperar
        botonRecuperar.setOnClickListener{
            val intencion = Intent(this, ActividadRecuperar::class.java)
            startActivity(intencion)
        }
    }

    private fun getUserFirebase() {
        val email: String? = auth.currentUser?.email
        if (email != null) {
            redirectToMain()
        }
    }

    private fun redirectToMain() {
        showHome()
        this.finish()
    }

    private fun authentication() {
        buttonEntrar.setOnClickListener {
            if (textEmail.text.isNotEmpty() && textClave.text.isNotEmpty()) {
                guardarDatosEnPreferencias()
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
        builder.setTitle(getString(R.string.error))
        builder.setMessage("Se ha producido un error autenticando al Usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.error))
        builder.setMessage("Campos de registro vacíos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, ActividadHome::class.java)
        startActivity(homeIntent)
    }

    private fun guardarDatosEnPreferencias() {
        val email = textEmail.text.toString()
        val clave = textClave.text.toString()
        val listadoAGrabar: Pair<String, String> = if (checkBoxRecordarme.isChecked) {
            email to clave
        } else {
            "" to ""
        }
        manejadorArchivo.saveInformation(listadoAGrabar)
    }

    private fun leerDatosDePreferencias() {
        val listadoLeido = manejadorArchivo.readInformation()
        checkBoxRecordarme.isChecked = true
        textEmail.setText(listadoLeido.first)
        textClave.setText(listadoLeido.second)
    }
}
