package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import com.hexagonal.vaquita.validador.Validador
import java.io.IOException

class ActividadRegistro : AppCompatActivity() {

    private val validador: Validador = Validador()
    private val gestionadorDeSubida: GestionadorDeSubida = GestionadorDeSubida()
    private var imageViewSubirFotoURL: Uri? = null
    private lateinit var auth: FirebaseAuth

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var imageViewSubirFoto: ImageView
    private lateinit var textNombre: EditText
    private lateinit var textNombreDeUsuario: EditText
    private lateinit var textEmail: EditText
    private lateinit var textTelefono: EditText
    private lateinit var textClaveInicial: EditText
    private lateinit var textClaveRepeticion: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_registro)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        //Inicializando Firebase Auth
        auth = Firebase.auth

        imageViewSubirFoto = findViewById(R.id.imageSubirImagen)
        textNombre = findViewById(R.id.textNombre)
        textNombreDeUsuario = findViewById(R.id.textNombreDeUsuario)
        textEmail = findViewById(R.id.textEmail)
        textTelefono = findViewById(R.id.telefono)
        textClaveInicial = findViewById(R.id.textClaveInicial)
        textClaveRepeticion = findViewById(R.id.textClaveRepeticion)

        var mailValido: Boolean
        textEmail.setOnClickListener OnClickListener@{
            if (!validador.esValidoCorreo(textEmail.text.toString())) {
                textEmail.error = getString(R.string.mailinvalido)
                mailValido = false
                return@OnClickListener
            }
            Toast.makeText(this, getString(R.string.mailvalido), Toast.LENGTH_SHORT).show()
            mailValido = true
            return@OnClickListener
        }

        var nombreValido: Boolean
        textNombre.setOnClickListener OnClickListener@{
            if (!(textNombre.text.toString()
                    .isNotEmpty() && validador.validarString(textNombre.text.toString()))
            ) {
                textNombre.error = "Nombre inválido"
                nombreValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Nombre válido", Toast.LENGTH_SHORT).show()
            nombreValido = true
            return@OnClickListener
        }

        var nombreDeUsuarioValido: Boolean
        textNombreDeUsuario.setOnClickListener OnClickListener@{
            if (textNombreDeUsuario.text.toString().isEmpty()) {
                textNombreDeUsuario.error = "Nombre de Usuario inválido"
                nombreDeUsuarioValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Nombre de Usuario válido", Toast.LENGTH_SHORT).show()
            nombreDeUsuarioValido = true
            return@OnClickListener
        }


        var telefonoValido: Boolean
        textTelefono.setOnClickListener OnClickListener@{
            if (!validador.validarTelefono(textTelefono.text.toString())) {
                textTelefono.error = "Teléfono inválido"
                telefonoValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Teléfono válido", Toast.LENGTH_SHORT).show()
            telefonoValido = true
            return@OnClickListener
        }


        var claveInicialValido: Boolean
        textClaveInicial.setOnClickListener OnClickListener@{
            if (!validador.esLongitudMayorOIgual(textClaveInicial.text.toString(), 8)) {
                textClaveInicial.error = "Clave inválida"
                claveInicialValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Clave válida", Toast.LENGTH_SHORT).show()
            claveInicialValido = true
            return@OnClickListener
        }


        var claveRepeticionValido: Boolean
        textClaveRepeticion.setOnClickListener OnClickListener@{
            if (textClaveInicial.text.toString() != textClaveRepeticion.text.toString()) {
                textClaveRepeticion.error = "Repetición de clave inválida"
                claveRepeticionValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Repetición de clave válida", Toast.LENGTH_SHORT).show()
            claveRepeticionValido = true
            return@OnClickListener
        }

        imageViewSubirFoto.setOnClickListener OnClickListener@{
            seleccionarImagen()
            //subir foto
            return@OnClickListener
        }


        // boton registro
        val botonRegistro = findViewById<Button>(R.id.botonRegistro)
        botonRegistro.setOnClickListener {
            //verifico

            if (!validador.esValidoCorreo(textEmail.text.toString())) {
                textEmail.error = "Mail inválido"
                mailValido = false
            } else {
                mailValido = true
            }

            if (!(textNombre.text.toString()
                    .isNotEmpty() && validador.validarString(textNombre.text.toString()))
            ) {
                textNombre.error = "Nombre inválido"
                nombreValido = false
            } else {
                nombreValido = true
            }

            if (textNombreDeUsuario.text.toString().isEmpty()) {
                textNombreDeUsuario.error = "Nombre de Usuario inválido"
                nombreDeUsuarioValido = false
            } else {
                nombreDeUsuarioValido = true
            }

            if (!validador.validarTelefono(textTelefono.text.toString())) {
                textTelefono.error = "Teléfono inválido"
                telefonoValido = false
            } else {
                telefonoValido = true
            }

            if (!validador.esLongitudMayorOIgual(textClaveInicial.text.toString(), 8)) {
                textClaveInicial.error = "Clave inválida"
                claveInicialValido = false
            } else {
                claveInicialValido = true
            }

            if (!textClaveInicial.text.toString().equals(textClaveRepeticion.text.toString())) {
                textClaveRepeticion.error = "Repetición de clave inválida"
                claveRepeticionValido = false
            } else {
                claveRepeticionValido = true
            }


            if (mailValido && nombreValido && nombreDeUsuarioValido && telefonoValido && claveInicialValido && claveRepeticionValido) {
                subirImagen()
                Toast.makeText(this, getString(R.string.espere), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.registrofallido), Toast.LENGTH_LONG).show()
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
                "Selecciona una imagen..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    private fun subirUsuario() {
        //Datos en firestore
        auth.createUserWithEmailAndPassword(
            textEmail.text.toString(),
            textClaveInicial.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val usuario = Usuario(
                    textNombre.text.toString(),
                    textEmail.text.toString(),
                    textNombreDeUsuario.text.toString(),
                    textTelefono.text.toString(),
                    imageViewSubirFotoURL.toString(),
                    emptyMap()
                )

                if (gestionadorDeSubida.subirDatosDeUsuarioNuevo(usuario, Firebase.firestore)) {
                    Toast.makeText(this, getString(R.string.registrofallido), Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }
                //builder del diálogo
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.exitoRegistro)
                    .setPositiveButton(
                        R.string.ok
                    ) { _, _ ->
                        //envío a inicio
                        val intencion = Intent(this, ActividadHome::class.java)
                        startActivity(intencion)
                    }
                builder.create()
                builder.show()
            }
        }
    }

    private fun subirImagen() {
        if (filePath != null) {
            val fileNameArray = filePath!!.lastPathSegment?.split("/")
            val fileName = fileNameArray?.get(fileNameArray.size - 1)
            val storageRef = storage.reference
            val riversRef = storageRef.child("images/${fileName}")
            val uploadTask = riversRef.putFile(filePath!!)


            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageViewSubirFotoURL = downloadUri
                }

                subirUsuario()
            }
        } else {
            Toast.makeText(this, "Debe subir una foto de perfil", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                imageViewSubirFoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }


}

