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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hexagonal.vaquita.entidades.Gasto
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import com.hexagonal.vaquita.validador.Validador
import java.io.IOException

class ActividadNuevaCartera : AppCompatActivity() {
    val validador: Validador = Validador()
    val gestionadorDeSubida: GestionadorDeSubida = GestionadorDeSubida()
    var imageViewSubirFotoURL: Uri? = null

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    lateinit var imageViewSubirFoto: ImageView
    lateinit var textNombre: EditText
    lateinit var textFecha: EditText
    lateinit var textLugar: EditText

    private lateinit var userauth : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_nueva_cartera)
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        imageViewSubirFoto = findViewById<ImageView>(R.id.imageSubirImagenWallet)
        textNombre = findViewById<EditText>(R.id.textWalletName)
        textFecha = findViewById<EditText>(R.id.textFecha)
        textLugar = findViewById<EditText>(R.id.textLugar)

        //TEXTO QUEMADO
        textNombre.setText("Listening Party de Comic")
        var nombreValido: Boolean = false
        textNombre.setOnClickListener OnClickListener@{
            if ((textNombre.text.toString()
                    .isNotEmpty() && validador.validarString(textNombre.text.toString()))
            ) {
                textNombre.setError("Nombre inválido")
                nombreValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Nombre válido", Toast.LENGTH_SHORT).show()
            nombreValido = true
            return@OnClickListener
        }

        //TEXTO QUEMADO
        textFecha.setText("1/10/2021")
        var fechaValida: Boolean = false
        textFecha.setOnClickListener OnClickListener@{
            if (textFecha.text.toString().isEmpty()) {
                textFecha.setError("Fecha inválido")
                fechaValida = false
                return@OnClickListener
            }
            Toast.makeText(this, "Fecha válido", Toast.LENGTH_SHORT).show()
            fechaValida = true
            return@OnClickListener
        }

        //TEXTO QUEMADO
        textLugar.setText("La casa del manaba")
        var lugarValido: Boolean = false
        textLugar.setOnClickListener OnClickListener@{
            if (textLugar.text.toString().isEmpty()) {
                textLugar.setError("Lugar inválido")
                lugarValido = false
                return@OnClickListener
            }
            Toast.makeText(this, "Lugar válido", Toast.LENGTH_SHORT).show()
            lugarValido = true
            return@OnClickListener
        }

        imageViewSubirFoto.setOnClickListener OnClickListener@{
            seleccionarImagen()
            //subir foto
            return@OnClickListener
        }

        // boton nueva wallet
        var botonNuevaWallet = findViewById<Button>(R.id.botonContinuar)
        botonNuevaWallet.setOnClickListener {

            if ((textNombre.text.toString()
                    .isNotEmpty() && validador.validarString(textNombre.text.toString()))
            ) {
                textNombre.setError("Nombre inválido")
                nombreValido = false
            } else {
                nombreValido = true
            }

            if (textLugar.text.toString().isEmpty()) {
                textLugar.setError("Lugar inválido")
                lugarValido = false
            } else {
                lugarValido = true
            }

            if (textFecha.text.toString().isEmpty()) {
                textFecha.setError("Lugar inválido")
                fechaValida = false
            } else {
                fechaValida = true
            }

            if (nombreValido && lugarValido && fechaValida) {

                subirImagen()
                Toast.makeText(this, "Espere un momento", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registro fallido", Toast.LENGTH_LONG).show()
            }


        }
    }

    fun subirWallet() {
        var mapaVacio: Map<String, Boolean> =  emptyMap();
        var mapaVacioGasto:Map<String, Boolean> =  emptyMap();
        var mapaVacioPago:Map<String, Boolean> =  emptyMap();

        userauth = Firebase.auth.currentUser!!
        var userEmail=userauth.email.toString()

        val db = Firebase.firestore
        var userId:String
        db.collection("Usuarios")
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                userId = document.id.toString();
                val wallet: Wallet = Wallet(textNombre.text.toString(),textFecha.text.toString(),textLugar.text.toString(),
                    userId,mapaVacio,mapaVacioGasto,mapaVacioPago,imageViewSubirFotoURL.toString())

                if (!gestionadorDeSubida.subirDatosDeWalletNueva(wallet, Firebase.firestore)) {
                    Toast.makeText(this, "Registro fallido", Toast.LENGTH_LONG).show()
                }
                else {
                    //builder del diálogo
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(R.string.exitoRegistro)
                        .setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                //envío a inicio
                                val intencion = Intent(this, ActividadContactos::class.java)
                                startActivity(intencion)
                            })
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
                    imageViewSubirFotoURL = downloadUri
                    Glide.with(this).load(imageViewSubirFotoURL.toString())
                        .into(imageViewSubirFoto!!)
                    subirWallet()
                } else {
                    Log.e("ERROR", task.toString())
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
                imageViewSubirFoto?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }



}