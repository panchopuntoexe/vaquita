package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Gasto(
    var nombre: String? = null,
    var valor: Double? = null,
    var fecha: String? = null,
    var tipo: String ? = null,
) : Parcelable {
    companion object {
        fun QuerySnapshot.toGasto(): Gasto? {
            try {
                return this.first().toObject(Gasto::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        fun DocumentSnapshot.toGasto(): Gasto? {
            try {
                return this.toObject(Gasto::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        private const val TAG = "Gasto"
    }
}