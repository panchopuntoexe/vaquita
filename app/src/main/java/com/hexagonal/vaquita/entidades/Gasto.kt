package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gasto(
    var nombre: String? = null,
    var valor: Double? = null,
    var fecha: String? = null,
    var tipo: String ? = null,
) : Parcelable {
    companion object {
        fun QuerySnapshot.toGasto(): Gasto? {
            return try {
                this.first().toObject(Gasto::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun DocumentSnapshot.toGasto(): Gasto? {
            return try {
                this.toObject(Gasto::class.java)
            } catch (e: Exception) {
                null
            }
        }

        private const val TAG = "Gasto"
    }
}