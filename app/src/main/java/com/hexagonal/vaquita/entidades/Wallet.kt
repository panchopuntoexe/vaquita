package com.hexagonal.vaquita.entidades


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Wallet(
    var nombre: String? = null,
    var fecha: String? = null,
    var lugar: String? = null,
    var creador: Usuario? = null,
    var usuarios: List<Usuario>? = null,
    var gastos: List<Gasto>? = null,
    var pagos: List<Pago>? = null
) : Parcelable

/*: Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("creador"),
        TODO("usuarios"),
        TODO("gastos"),
        TODO("pagos")
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Wallet> {
        override fun createFromParcel(parcel: Parcel): Wallet {
            return Wallet(parcel)
        }

        override fun newArray(size: Int): Array<Wallet?> {
            return arrayOfNulls(size)
        }
    }
}*/