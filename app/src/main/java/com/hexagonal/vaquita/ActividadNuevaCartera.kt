package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import java.io.IOException

class ActividadNuevaCartera : AppCompatActivity() {
    private val gestionadorDeSubida: GestionadorDeSubida = GestionadorDeSubida()
    private var imageViewSubirFotoURL: Uri? = null

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var imageViewSubirFoto: ImageView
    private lateinit var textNombre: EditText
    private lateinit var textFecha: EditText
    private lateinit var textLugar: EditText

    private lateinit var userauth: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_nueva_cartera)
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        imageViewSubirFoto = findViewById(R.id.imageSubirImagenWallet)
        textNombre = findViewById(R.id.textWalletName)
        textFecha = findViewById(R.id.textFecha)
        textLugar = findViewById(R.id.textLugar)

        var nombreValido: Boolean
        textNombre.setOnClickListener OnClickListener@{
            if (textNombre.text.toString().isEmpty()){
                textNombre.error = getString(R.string.nombreInvalido)
                nombreValido = false
                return@OnClickListener
            }
            Toast.makeText(this, getString(R.string.nombreInvalido), Toast.LENGTH_SHORT).show()
            nombreValido = true
            return@OnClickListener
        }

        var fechaValida: Boolean
        textFecha.setOnClickListener OnClickListener@{
            if (textFecha.text.toString().isEmpty()) {
                textFecha.error = getString(R.string.fechaInvalida)
                fechaValida = false
                return@OnClickListener
            }
            Toast.makeText(this, getString(R.string.fechaValida), Toast.LENGTH_SHORT).show()
            fechaValida = true
            return@OnClickListener
        }

        var lugarValido: Boolean
        textLugar.setOnClickListener OnClickListener@{
            if (textLugar.text.toString().isEmpty()) {
                textLugar.error = getString(R.string.lugarInvalida)
                lugarValido = false
                return@OnClickListener
            }
            Toast.makeText(this, getString(R.string.lugarValida), Toast.LENGTH_SHORT).show()
            lugarValido = true
            return@OnClickListener
        }

        imageViewSubirFoto.setOnClickListener OnClickListener@{
            seleccionarImagen()
            //subir foto
            return@OnClickListener
        }

        // boton nueva wallet
        val botonNuevaWallet = findViewById<Button>(R.id.botonContinuar)
        botonNuevaWallet.setOnClickListener {

            nombreValido = if (textNombre.text.toString().isEmpty()){
                textNombre.error = getString(R.string.nombreInvalido)
                false
            } else {
                true
            }

            lugarValido = if (textLugar.text.toString().isEmpty()) {
                textLugar.error = getString(R.string.lugarInvalida)
                false
            } else {
                true
            }

            if (textFecha.text.toString().isEmpty()) {
                textFecha.error = getString(R.string.fechaInvalida)
                fechaValida = false
            } else {
                fechaValida = true
            }

            if (nombreValido && lugarValido && fechaValida) {

                subirImagen()
                Toast.makeText(this, getString(R.string.espere), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.registrofallido), Toast.LENGTH_LONG).show()
            }


        }

        // Botón Cancelar
        val botonCancelar = findViewById<Button>(R.id.btnCancelarCartera)
        botonCancelar.setOnClickListener {
            super.onBackPressed()
        }

    }

    private fun subirWallet() {
        val mapaVacio: Map<String, Boolean> = emptyMap()
        val mapaVacioGasto: Map<String, Boolean> = emptyMap()
        val mapaVacioPago: Map<String, Boolean> = emptyMap()

        userauth = Firebase.auth.currentUser!!
        val userEmail = userauth.email.toString()

        val db = Firebase.firestore
        var userId: String
        db.collection(getString(R.string.usuarios))
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                userId = document.id
                val wallet = Wallet(
                    textNombre.text.toString(),
                    textFecha.text.toString(),
                    textLugar.text.toString(),
                    userId,
                    mapaVacio,
                    mapaVacioGasto,
                    mapaVacioPago,
                    imageViewSubirFotoURL.toString()
                )

                if (!gestionadorDeSubida.subirDatosDeWalletNueva(wallet, Firebase.firestore)) {
                    Toast.makeText(this, getString(R.string.registrofallido), Toast.LENGTH_LONG).show()
                } else {
                    //builder del diálogo
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(R.string.exitoRegistro)
                        .setPositiveButton(R.string.ok
                        ) { _, _ ->
                            //envío a inicio
                            val intencion = Intent(this, ActividadContactos::class.java)
                            intencion.putExtra(
                                "walletId",
                                GestionadorDeSubida.walletId
                            )
                            intencion.putExtra(
                                "propietario",
                                GestionadorDeSubida.propietario
                            )
                            startActivity(intencion)
                        }
                    builder.create()
                    builder.show()
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
                    subirWallet()
                }
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