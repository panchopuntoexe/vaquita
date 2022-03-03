package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize

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
            return try {
                this.first().toObject(Pago::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun DocumentSnapshot.toPago(): Pago? {
            return try {
                this.toObject(Pago::class.java)
            } catch (e: Exception) {
                null
            }
        }


        private const val TAG = "Gasto"
    }

    fun pagoToGasto(): Gasto {
        return Gasto(nombre, valor, fecha, tipo)
    }

}