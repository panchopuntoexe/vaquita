package com.hexagonal.vaquita

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth


class ActividadRecuperar : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var recuperar: Button
    private lateinit var progress: ProgressDialog
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_recuperar)

        email = findViewById(R.id.textEmailRecuperar)
        recuperar = findViewById(R.id.botonRecuperar)

        auth = FirebaseAuth.getInstance()
        progress = ProgressDialog(this)

        getRecuperar()
    }

    private fun getRecuperar() {
        recuperar.setOnClickListener {
            if (email.text.isNotEmpty()) {
                progress.setMessage("Espere un momento...")
                progress.setCanceledOnTouchOutside(false)
                progress.show()
                getEnviarCorreo()
            } else {
                showAlert()
            }
        }
    }

    private fun getEnviarCorreo() {
        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(email.text.toString().trim()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showMessage()
            }else{
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.error))
        builder.setMessage("El correo no se pudo enviar")
        builder.setPositiveButton(getString(R.string.aceptar), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMessage() {
        Toast.makeText(
            this,
            "Por favor revise su correo para restaurar la contraseña",
            Toast.LENGTH_LONG
        ).show()
        val loginIntent = Intent(this, ActividadLogin::class.java)
        startActivity(loginIntent)
        finish()
    }
}