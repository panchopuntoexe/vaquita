package com.hexagonal.vaquita.validador

import android.telephony.PhoneNumberUtils
import android.util.Patterns
import java.util.regex.Pattern

class Validador {


    fun esValidoCorreo(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun esLongitudMayorOIgual(cadenaAValidar: String, longitud: Int): Boolean {
        if (cadenaAValidar.length >= longitud) {
            return true
        }
        return false
    }

    fun esLongitudIgual(cadenaAValidar: String, longitud: Int): Boolean {
        if (cadenaAValidar.length == longitud) {
            return true
        }
        return false
    }

    fun esLongitudMenor(cadenaAValidar: String, longitud: Int): Boolean {
        if (cadenaAValidar.length <= longitud) {
            return true
        }
        return false
    }


    fun validarString(cadena:String):Boolean{
        for (caracter in cadena)
        {
            if (caracter !in 'A'..'Z' && caracter !in 'a'..'z'&& !caracter.equals(' ')) {
                return false
            }
        }
        return true
    }

    //valida números, el +, si hay caracteres retorna false
    fun validarTelefono(telefono: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(telefono)
    }


}