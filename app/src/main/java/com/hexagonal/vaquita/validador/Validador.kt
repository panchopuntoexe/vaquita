package com.hexagonal.vaquita.validador

import android.telephony.PhoneNumberUtils
import android.util.Patterns
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class Validador {

    constructor()


    public fun esValidoCorreo(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    public fun esLongitudMayorOIgual(cadenaAValidar:String,longitud:Int):Boolean{
        if (cadenaAValidar.length>=longitud) {
            return true
        }
        return false
    }

    public fun validarString(cadena:String):Boolean{
        for (caracter in cadena)
        {
            if (caracter !in 'A'..'Z' && caracter !in 'a'..'z') {
                return false
            }
        }
        return true
    }

    //valida números, el +, si hay caracteres retorna false
    public fun validarTelefono(telefono:String):Boolean{
        return PhoneNumberUtils.isGlobalPhoneNumber(telefono)
    }


}