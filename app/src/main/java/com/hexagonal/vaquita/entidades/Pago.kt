package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Pago(
    var nombre: String? = null,
    var valor: Double? = null,
    var user: String? = null,
    var fecha: String? = null,
    var tipo: String? = null,
) : Parcelable {
    companion object {
        fun QuerySnapshot.toPago(): Pago? {
            try {
                return this.first().toObject(Pago::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        fun DocumentSnapshot.toPago(): Pago? {
            try {
                return this.toObject(Pago::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }


        private const val TAG = "Gasto"
    }

    fun pagoToGasto(): Gasto {
        return Gasto(nombre, valor, fecha, tipo)
    }

}