package com.hexagonal.vaquita.entidades

data class Pago (
    var nombre: String? = null,
    var valor: Double? = null,
    var user: Usuario? = null
)