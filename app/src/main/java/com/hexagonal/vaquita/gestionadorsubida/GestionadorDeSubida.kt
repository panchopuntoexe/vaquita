package com.hexagonal.vaquita.gestionadorsubida

import android.util.Log
import android.util.Patterns
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.entidades.Usuario
import java.util.regex.Pattern

class GestionadorDeSubida {

    constructor() {

    }

    public fun subirDatosDeUsuarioNuevo(usuario: Usuario, db: FirebaseFirestore): Boolean {
        var retorno: Boolean = false
        var mapaVacio:Map<String,Boolean> = emptyMap<String,Boolean>()
        val user = hashMapOf(
            "correo" to usuario.correo,
            "foto" to usuario.foto,
            "nombre" to usuario.nombre,
            "telefono" to usuario.telefono,
            "username" to usuario.username,
            "wallets" to mapaVacio
        )
        val TAG = "MENSAJE"
        db.collection("Usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                retorno = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                retorno = false
            }

        return retorno
    }

}