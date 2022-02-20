package com.hexagonal.vaquita.entidades

import com.hexagonal.vaquita.entidades.Wallet

data class Usuario(
    var nombre: String? = null,
    var correo: String? = null,
    var username: String? = null,
    var telefono: String? = null,
    var foto: String? = null
)