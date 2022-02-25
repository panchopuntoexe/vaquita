package com.hexagonal.vaquita.entidades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.parceler.Parcel

@Parcelize
data class Usuario(
    var nombre: String? = null,
    var correo: String? = null,
    var username: String? = null,
    var telefono: String? = null,
    var foto: String? = null,
    var wallets:MutableMap<String,Boolean>?=null
) : Parcelable