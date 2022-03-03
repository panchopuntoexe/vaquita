package com.hexagonal.vaquita.entidades


import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallet(
    var nombre: String? = null,
    var fecha: String? = null,
    var lugar: String? = null,
    var creador: String? = null,
    //var usuarios: ArrayList<Usuario>? = null,
    var users: Map<String,Boolean>? = null,
    var gastos: Map<String,Boolean>? = null,
    var pagos: Map<String,Boolean>? = null,
    var foto: String? = null
) : Parcelable

{
    companion object {
        fun DocumentSnapshot.toWallet(): Wallet? {
            return try {
                this.toObject(Wallet::class.java)
            } catch (e: Exception) {
                null
            }
        }
        private const val TAG = "User"
    }
}