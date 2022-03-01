package com.hexagonal.vaquita.gestionadorsubida

import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import java.util.regex.Pattern

class GestionadorDeSubida {

    companion object {
        lateinit var walletId: String
    }

    constructor() {

    }

    public fun subirDatosDeUsuarioNuevo(usuario: Usuario, db: FirebaseFirestore): Boolean {
        var retorno: Boolean = false
        var mapaVacio: Map<String, Boolean> = emptyMap<String, Boolean>()
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
        var mapaVacio: Map<String, Boolean> = emptyMap<String, Boolean>()
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
                walletId = documentReference.id
                val db = Firebase.firestore
                var userfb: Usuario
                db.collection("Usuarios")
                    .document(wallet.creador!!)
                    .get()
                    .addOnSuccessListener { document ->
                        userfb = document.toObject(Usuario::class.java)!!
                        var mapaAux: MutableMap<String, Boolean> =
                            userfb.wallets as MutableMap<String, Boolean>
                        mapaAux.put(documentReference.id, true)
                        db.collection("Usuarios").document(wallet.creador!!)
                            .update("wallets", mapaAux)
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully updated!"
                                )
                            }
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

    public fun subirPago(pago: Pago, wallet: Wallet): Boolean {
        val db = Firebase.firestore
        val nombre: String = pago.nombre.toString()
        val fecha: String = pago.fecha.toString()
        val valor: Double? = pago.valor
        val user: String = pago.user.toString()
        Log.d("data", nombre + fecha + valor)

        if (nombre.isNotEmpty() && fecha.isNotEmpty() && valor != null && user.isNotEmpty()) {
            var retorno: Boolean = true
            val pagoNuevo = hashMapOf(
                "nombre" to nombre,
                "fecha" to fecha,
                "valor" to valor,
                "tipo" to "pago",
                "user" to user
            )
            val TAG = "MENSAJE"
            db.collection("Pagos")
                .add(pagoNuevo)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Pago added with ID: ${documentReference.id}")
                    val pagoId = documentReference.id;
                    db.collection("Wallets")
                        .whereEqualTo("foto", wallet.foto)
                        .get()
                        .addOnSuccessListener { documentReference1 ->
                            val docId = documentReference1.first().id
                            val mapaPagos = documentReference1.first()
                                .get("pagos") as MutableMap<String, Boolean>
                            mapaPagos.put(pagoId, true)
                            db.collection("Wallets").document(docId)
                                .update("pagos", mapaPagos)
                            retorno = true
                        }
                    retorno = true
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    retorno = false
                }
            return retorno
        } else {
            return false
        }
    }

}