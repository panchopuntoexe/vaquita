package com.hexagonal.vaquita.datos

interface FileHandler {
    fun saveInformation(datosAGrabar:Pair<String,String>)
    fun readInformation():Pair<String,String>
}