package com.hexagonal.vaquita.entidades

import android.os.Parcelable
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
    var wallets: Map<String, Boolean>? = null,
) : Parcelable {
    companion object {
        fun QuerySnapshot.toUser(): Usuario? {
            return try {
                this.first().toObject(Usuario::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun DocumentSnapshot.toUser(): Usuario? {
            return try {
                this.toObject(Usuario::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun DocumentSnapshot.toMapUser(): MutableMap<String, String>? {
            return try {
                mutableMapOf(Pair(this.id, this.get("correo").toString()))
            } catch (e: Exception) {
                null
            }
        }

        fun QuerySnapshot.toUserId(): String? {
            return try {
                this.first().id
            } catch (e: Exception) {
                null
            }
        }

        private const val TAG = "User"
    }
}
