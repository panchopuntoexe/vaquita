package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Gasto(
    var nombre: String? = null,
    var valor: Double? = null,
    var fecha: String? = null
) : Parcelable