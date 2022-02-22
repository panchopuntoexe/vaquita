package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Pago (
    var nombre: String? = null,
    var valor: Double? = null,
    var user: Usuario? = null
) : Parcelable