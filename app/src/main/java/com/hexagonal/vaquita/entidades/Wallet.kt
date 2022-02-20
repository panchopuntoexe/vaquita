package com.hexagonal.vaquita.entidades


data class Wallet(
    var nombre: String? = null,
    var fecha: String? = null,
    var lugar: String? = null,
    var creador: Usuario? = null,
    var usuarios: List<Usuario>? = null,
    var gastos: List<Gasto>? = null,
    var pagos: List<Pago>? = null
)