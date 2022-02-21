package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.validador.Validador
import java.io.IOException
import java.net.URI

class ActividadRegistro : AppCompatActivity() {

    val validador: Validador = Validador()
    var imageViewSubirFotoURL:Uri? = null


    // Uri indicates, where the image will be picked from
    private var filePath: Uri? =null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    lateinit var imageViewSubirFoto :ImageView
    lateinit var textNombre :EditText
    lateinit var textNombreDeUsuario:EditText
    lateinit var textEmail:EditText
    lateinit var textTelefono:EditText
    lateinit var textClaveInicial:EditText
    lateinit var textClaveRepeticion:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_registro)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        imageViewSubirFoto = findViewById<ImageView>(R.id.imageSubirImagen)
        textNombre = findViewById<EditText>(R.id.textNombre)
        textNombreDeUsuario = findViewById<EditText>(R.id.textNombreDeUsuario)
        textEmail = findViewById<EditText>(R.id.textEmail)
        textTelefono = findViewById<EditText>(R.id.telefono)
        textClaveInicial = findViewById<EditText>(R.id.textClaveInicial)
        textClaveRepeticion = findViewById<EditText>(R.id.textClaveRepeticion)

        textEmail.setText("francisco.garcia@epn.edu.ec")
        var mailValido: Boolean = false
        textEmail.setOnClickListener OnClickListener@{
            if (!validador.esValidoCorreo(textEmail.text.toString())) {
                textEmail.setError("Mail inválido")
                mailValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Mail válido", Toast.LENGTH_SHORT).show()
            mailValido = true
            return@OnClickListener
        }

        textNombre.setText("Francisco")
        var nombreValido: Boolean = false
        textNombre.setOnClickListener OnClickListener@{
            if (!(textNombre.text.toString().isNotEmpty()&&validador.validarString(textNombre.text.toString()))) {
                textNombre.setError("Nombre inválido")
                nombreValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Nombre válido", Toast.LENGTH_SHORT).show()
            nombreValido = true
            return@OnClickListener
        }

        textNombreDeUsuario.setText("comic.ec")
        var nombreDeUsuarioValido: Boolean = false
        textNombreDeUsuario.setOnClickListener OnClickListener@{
            if (textNombreDeUsuario.text.toString().isEmpty()) {
                textNombreDeUsuario.setError("Nombre de Usuario inválido")
                nombreDeUsuarioValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Nombre de Usuario válido", Toast.LENGTH_SHORT).show()
            nombreDeUsuarioValido = true
            return@OnClickListener
        }

        textTelefono.setText("0990595018")
        var telefonoValido: Boolean = false
        textTelefono.setOnClickListener OnClickListener@{
            if (!validador.validarTelefono(textTelefono.text.toString())) {
                textTelefono.setError("Teléfono inválido")
                telefonoValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Teléfono válido", Toast.LENGTH_SHORT).show()
            telefonoValido = true
            return@OnClickListener
        }

        textClaveInicial.setText("123456789")
        var claveInicialValido: Boolean = false
        textClaveInicial.setOnClickListener OnClickListener@{
            if (!validador.esLongitudMayorOIgual(textClaveInicial.text.toString(),8)) {
                textClaveInicial.setError("Clave inválida")
                claveInicialValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Clave válida", Toast.LENGTH_SHORT).show()
            claveInicialValido = true
            return@OnClickListener
        }

        textClaveRepeticion.setText("123456789")
        var claveRepeticionValido: Boolean = false
        textClaveRepeticion.setOnClickListener OnClickListener@{
            if (textClaveInicial.text.toString() != textClaveRepeticion.text.toString()) {
                textClaveRepeticion.setError("Repetición de clave inválida")
                claveRepeticionValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Repetición de clave válida", Toast.LENGTH_SHORT).show()
            claveRepeticionValido = true
            return@OnClickListener
        }

        imageViewSubirFoto.setOnClickListener OnClickListener@{
            seleccionarImagen()
            subirImagen()

            //subir foto
            return@OnClickListener
        }


        // boton registro
        var botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener {
            //verifico

            if (!validador.esValidoCorreo(textEmail.text.toString())) {
                textEmail.setError("Mail inválido")
                mailValido = false
            } else {
                mailValido = true
            }

            if (!(textNombre.text.toString().isNotEmpty()&&validador.validarString(textNombre.text.toString()))) {
                textNombre.setError("Nombre inválido")
                nombreValido = false
            } else {
                nombreValido = true
            }

            if (textNombreDeUsuario.text.toString().isEmpty()) {
                textNombreDeUsuario.setError("Nombre de Usuario inválido")
                nombreDeUsuarioValido = false
            } else {
                nombreDeUsuarioValido = true
            }

            if (!validador.validarTelefono(textTelefono.text.toString())) {
                textTelefono.setError("Teléfono inválido")
                telefonoValido = false
            } else {
                telefonoValido = true
            }

            if (!validador.esLongitudMayorOIgual(textClaveInicial.text.toString(),8)) {
                textClaveInicial.setError("Clave inválida")
                claveInicialValido = false
            } else {
                claveInicialValido = true
            }

            if (!textClaveInicial.text.toString().equals(textClaveRepeticion.text.toString())) {
                textClaveRepeticion.setError("Repetición de clave inválida")
                claveRepeticionValido = false
            } else {
                claveRepeticionValido = true
            }

            if (mailValido && nombreValido && nombreDeUsuarioValido && telefonoValido && claveInicialValido && claveRepeticionValido) {
                //Datos en firestore

                val usuario:Usuario = Usuario(textNombre.text.toString(),
                                                textEmail.text.toString(),
                                                textNombreDeUsuario.text.toString(),
                                                textTelefono.text.toString(),
                                                imageViewSubirFotoURL.toString())
                //builder del diálogo
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.exitoRegistro)
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialog, id ->
                            //envío a inicio
                            val intencion = Intent(this, ActividadHome::class.java)
                            startActivity(intencion)
                        })/*
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })*/
                builder.create()
                builder.show()
            } else {
                Toast.makeText(this, "Registro fallido", Toast.LENGTH_LONG).show()
            }


        }
    }

    // Select Image method
    private fun seleccionarImagen() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Selcciona una imagen..."
            ),
            PICK_IMAGE_REQUEST
        )
    }



    fun subirImagen() {
        if (filePath != null) {
            val fileNameArray = filePath!!.lastPathSegment?.split("/")
            val fileName = fileNameArray?.get(fileNameArray.size - 1)
            var storageRef = storage.reference
            val riversRef = storageRef.child("images/${fileName}")
            var uploadTask = riversRef.putFile(filePath!!)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageViewSubirFotoURL=downloadUri
                    Glide.with(this).load(imageViewSubirFotoURL.toString()).into(imageViewSubirFoto!!)
                    Log.d("MENSAJE",downloadUri.toString())
                } else {
                    Log.e("ERROR", task.toString())
                }
            }
        }
    }
}

