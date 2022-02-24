package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.gestionadorsubida.GestionadorDeSubida
import com.hexagonal.vaquita.validador.Validador

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

        textNombre.setText("Listening Party de Comic")
        var nombreValido: Boolean = false
        textNombre.setOnClickListener OnClickListener@{
            if (!(textNombre.text.toString()
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

            if (!(textNombre.text.toString()
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

    fun subirUsuario() {

        val usuario: Usuario? =null;//TODO leer este usuario de la base de datos
        var mapaVacio:List<Usuario> =  emptyList();
        var mapaVacioGasto:List<Gasto> =  emptyList();
        var mapaVacioPago:List<Pago> =  emptyList();
        val wallet: Wallet = Wallet(textNombre.text.toString(),textFecha.text.toString(),textLugar.text.toString(),
        usuario,mapaVacio,mapaVacioGasto,mapaVacioPago)

        if (gestionadorDeSubida.subirDatosDeUsuarioNuevo(usuario, Firebase.firestore)) {
            Toast.makeText(this, "Registro fallido", Toast.LENGTH_LONG).show()
            return@addOnCompleteListener
        }
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
                    //Log.d("MENSAJE", downloadUri.toString())
                } else {
                    Log.e("ERROR", task.toString())
                }
            }
        } else {
            Toast.makeText(this, "Debe subir una foto de perfil", Toast.LENGTH_LONG).show()
        }
    }

}