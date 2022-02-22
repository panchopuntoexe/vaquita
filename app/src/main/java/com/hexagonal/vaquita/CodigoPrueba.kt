package com.hexagonal.vaquita

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*


class CodigoPrueba : AppCompatActivity() {
    private lateinit var btnSelect: Button
    private lateinit var btnUpload: Button

    // view for image view
    private lateinit var imageView: ImageView

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? =null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo_prueba)
        val colorDrawable = ColorDrawable(
            Color.parseColor("#0F9D58")
        )
        imageView = findViewById<View>(R.id.imgView) as ImageView

        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/proyectocazarpatos-e0106.appspot.com/o/images%2F51?alt=media&token=f51dccf2-0eb9-45b1-9e2e-39e2e52c1cf0").into(imageView!!)

        // initialise views
        btnSelect = findViewById(R.id.btnChoose)
        btnUpload = findViewById(R.id.btnUpload)
        imageView = findViewById(R.id.imgView)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        // on pressing btnSelect SelectImage() is called

        btnSelect.setOnClickListener {
            SelectImage()
        }
        // on pressing btnUpload uploadImage() is called

        btnUpload.setOnClickListener {
            //uploadImage()
            subirImagen()
        }
    }

    // Select Image method
    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    // Override onActivityResult method
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
                imageView?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    fun subirImagen() {
        if (filePath != null) {
            val fileNameArray = filePath!!.lastPathSegment?.split("/")
            val fileName = fileNameArray?.get(fileNameArray.size - 1)
            var storageRef = storage.reference
            val riversRef = storageRef.child("images/${fileName}")
            var uploadTask = riversRef.putFile(filePath!!)
            // Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener {
//                Toast.makeText(this, "Error al subir el archivo", Toast.LENGTH_SHORT).show()
//            }.addOnSuccessListener { taskSnapshot ->
//                val url= taskSnapshot.storage.downloadUrl.getResult().toString()
//                Toast.makeText(this, "Archivo subido con exito!", Toast.LENGTH_SHORT).show()
//                Log.d("URL",url)
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val downloadUri = task.result
//                    Log.d("MENSAJE", "")
//                } else {
//                    Log.e("ERROR", task.toString())
//                }
//            }
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
                    Log.d("URL",downloadUri.toString())
                } else {
                    Log.e("ERROR", task.toString())
                }
            }
        }
    }

    // UploadImage method
    private fun uploadImage() {
        if (filePath != null) {

//            // Code for showing progressDialog while uploading
//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading...")
//            progressDialog.show()
//
//            // Defining the child of storageReference
//
//            var ref: StorageReference = storageReference
//                ?.child(
//                    "images/"
//                            + UUID.randomUUID().toString()
//                ) ?:
//
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(filePath)
//                .addOnSuccessListener(
//                    OnSuccessListener<Any?> { // Image uploaded successfully
//                        // Dismiss dialog
//                        progressDialog.dismiss()
//                        Toast
//                            .makeText(
//                                this@CodigoPrueba,
//                                "Image Uploaded!!",
//                                Toast.LENGTH_SHORT
//                            )
//                            .show()
//                    })
//                .addOnFailureListener(OnFailureListener { e -> // Error, Image not uploaded
//                    progressDialog.dismiss()
//                    Toast
//                        .makeText(
//                            this@CodigoPrueba,
//                            "Failed " + e.message,
//                            Toast.LENGTH_SHORT
//                        )
//                        .show()
//                })
//                .addOnProgressListener(
//                    OnProgressListener<Any> { taskSnapshot ->
//
//                        // Progress Listener for loading
//                        // percentage on the dialog box
//                        val progress: Double = (100.0
//                                * taskSnapshot.getBytesTransferred()
//                                / taskSnapshot.getTotalByteCount())
//                        progressDialog.setMessage(
//                            "Uploaded "
//                                    + progress.toInt() + "%"
//                        )
//                    })
            var storageRef = storage.reference
            val mountainsRef = storageRef.child(filePath.toString())

// Create a reference to 'images/mountains.jpg'
            val mountainImagesRef = storageRef.child("images/" + filePath.toString())
            Toast.makeText(this, filePath.toString(), Toast.LENGTH_SHORT).show()

// While the file names are the same, the references point to different files
            mountainsRef.name == mountainImagesRef.name // true
            mountainsRef.path == mountainImagesRef.path // false
        }
    }
}