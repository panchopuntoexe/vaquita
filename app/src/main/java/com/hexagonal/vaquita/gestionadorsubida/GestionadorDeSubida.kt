package com.hexagonal.vaquita.gestionadorsubida

import android.util.Log
import android.util.Patterns
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
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



    public fun subirDatosDeWalletNueva(wallet: Wallet, db: FirebaseFirestore): Boolean {
        var retorno: Boolean = true
        var mapaVacio:Map<String,Boolean> = emptyMap<String,Boolean>()
        val walletNueva = hashMapOf(
            "creador" to wallet.creador,
            "fecha" to wallet.fecha,
            "gastos" to wallet.gastos,
            "lugar" to wallet.lugar,
            "nombre" to wallet.nombre,
            "pagos" to wallet.pagos,
            "users" to wallet.users,
            "foto" to wallet.foto
        )
        val TAG = "MENSAJE"
        db.collection("Wallets")
            .add(walletNueva)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Wallet added with ID: ${documentReference.id}")
                val db = Firebase.firestore
                var userfb : Usuario
                db.collection("Usuarios")
                    .document(wallet.creador!!)
                    .get()
                    .addOnSuccessListener { document ->
                        userfb = document.toObject(Usuario::class.java)!!
                        userfb.wallets?.plus(Pair(documentReference.id,true))
                        db.collection("Usuarios").document(wallet.creador!!)
                            .update("wallets", userfb.wallets)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


                    }
                retorno = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                retorno = false
            }

        return retorno
    }

}