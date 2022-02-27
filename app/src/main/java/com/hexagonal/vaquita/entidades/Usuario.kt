package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    var nombre: String? = null,
    var correo: String? = null,
    var username: String? = null,
    var telefono: String? = null,
    var foto: String? = null,
    var wallets: Map<String,Boolean>? = null,
) : Parcelable {
    companion object {
        fun QuerySnapshot.toUser(): Usuario? {
            try {
                return this.first().toObject(Usuario::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        fun DocumentSnapshot.toUser(): Usuario? {
            try {
                return this.toObject(Usuario::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }
        private const val TAG = "User"
    }
}
