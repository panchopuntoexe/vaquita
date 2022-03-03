package com.hexagonal.vaquita.gestionadorsubida

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.ActividadHome
import com.hexagonal.vaquita.datos.PAGOS
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.datos.WALLETS
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet

class GestionadorDeSubida {

    companion object {
        lateinit var walletId: String
        lateinit var propietario: String
    }

    fun subirDatosDeUsuarioNuevo(usuario: Usuario, db: FirebaseFirestore): Boolean {
        var retorno = false
        val mapaVacio: Map<String, Boolean> = emptyMap()
        val user = hashMapOf(
            "correo" to usuario.correo,
            "foto" to usuario.foto,
            "nombre" to usuario.nombre,
            "telefono" to usuario.telefono,
            "username" to usuario.username,
            "wallets" to mapaVacio
        )
        db.collection(USUARIOS)
            .add(user)
            .addOnSuccessListener {
                retorno = true
            }
            .addOnFailureListener {
                retorno = false
            }

        return retorno
    }


    fun subirDatosDeWalletNueva(wallet: Wallet, db: FirebaseFirestore): Boolean {
        var retorno = true
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

        db.collection(WALLETS)
            .add(walletNueva)
            .addOnSuccessListener { documentReference ->
                walletId = documentReference.id
                propietario = wallet.creador.toString()
                var userfb: Usuario
                db.collection(USUARIOS)
                    .document(wallet.creador!!)
                    .get()
                    .addOnSuccessListener { document ->
                        userfb = document.toObject(Usuario::class.java)!!
                        val mapaAux: MutableMap<String, Boolean> =
                            userfb.wallets as MutableMap<String, Boolean>
                        mapaAux[documentReference.id] = true
                        db.collection(USUARIOS).document(wallet.creador!!)
                            .update(WALLETS, mapaAux)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { }
                    }
                retorno = true
            }
            .addOnFailureListener {
                retorno = false
            }

        return retorno
    }

    fun subirPago(context: Context, pago: Pago, wallet: Wallet) {
        val db = Firebase.firestore
        val nombre: String = pago.nombre.toString()
        val fecha: String = pago.fecha.toString()
        val valor: Double? = pago.valor
        val user: String = pago.user.toString()

        if (nombre.isNotEmpty() && fecha.isNotEmpty() && valor != null && user.isNotEmpty()) {
            val pagoNuevo = hashMapOf(
                "nombre" to nombre,
                "fecha" to fecha,
                "valor" to valor,
                "tipo" to "pago",
                "user" to user
            )
            db.collection(PAGOS)
                .add(pagoNuevo)
                .addOnSuccessListener { documentReference ->
                    val pagoId = documentReference.id
                    db.collection(WALLETS)
                        .whereEqualTo("foto", wallet.foto)
                        .get()
                        .addOnSuccessListener { documentReference1 ->
                            val docId = documentReference1.first().id
                            val mapaPagos = documentReference1.first()
                                .get("pagos") as MutableMap<String, Boolean>
                            mapaPagos[pagoId] = true
                            db.collection(WALLETS).document(docId)
                                .update("pagos", mapaPagos)
                            val intencion = Intent(context, ActividadHome::class.java)
                            context.startActivity(intencion)
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al realizar el Pago", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

}