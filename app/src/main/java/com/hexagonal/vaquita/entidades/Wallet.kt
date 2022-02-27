package com.hexagonal.vaquita.entidades


import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

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
) : Parcelable

{
    companion object {
        fun DocumentSnapshot.toWallet(): Wallet? {
            try {
                return this.toObject(Wallet::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }
        private const val TAG = "User"
    }
}