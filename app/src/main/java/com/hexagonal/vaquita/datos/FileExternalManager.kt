package com.hexagonal.vaquita.datos

import android.app.Activity
import android.os.Environment
import com.hexagonal.vaquita.SHAREDINFO_FILENAME
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileExternalManager(val actividad: Activity): FileHandler{

    // Comprobando que el almacenamiento externo sea escribible
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Guardando la Información
    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (isExternalStorageWritable()) {
            FileOutputStream(
                File(
                    actividad.getExternalFilesDir(null),
                    SHAREDINFO_FILENAME
                )
            ).bufferedWriter().use { outputStream ->
                outputStream.write(datosAGrabar.first)
                outputStream.write(System.lineSeparator())
                outputStream.write(datosAGrabar.second)
            }
        }
    }

    // Comprobación si el almacenamiento externo es leíble
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    // Lectura de la Información
    override fun ReadInformation(): Pair<String, String> {
        var email=""
        var clave=""
        try {
            if (isExternalStorageReadable()) {
                FileInputStream(
                    File(
                        actividad.getExternalFilesDir(null),
                        SHAREDINFO_FILENAME
                    )
                ).bufferedReader().use {
                    val datoLeido = it.readText()
                    val textArray = datoLeido.split(System.lineSeparator())
                    email = textArray[0]
                    clave = textArray[1]
                }
            }
            return (email to clave)
        }catch (e:Exception){
            return "" to ""
        }
    }
}