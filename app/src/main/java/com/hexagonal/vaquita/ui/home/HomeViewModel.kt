package com.hexagonal.vaquita.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUser
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.entidades.Wallet.Companion.toWallet
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeViewModel : ViewModel() {

    val TAG = "Wallets"
    val db = Firebase.firestore
    private val _userProfile = MutableLiveData<Usuario>()
    val _userWallets = MutableLiveData<List<Wallet>>()
    var userActual: LiveData<Usuario>? = _userProfile
    var wallets: LiveData<List<Wallet>>? = _userWallets

    init {
        viewModelScope.launch {
            val userEmail: String? = Firebase.auth.currentUser?.email
           // val parser = JsonParser()
           // val jsonElement = parser.parse(getProfileData(userEmail)!!.wallets.toString())
           // val carteras: JsonObject = jsonElement.asJsonObject
            _userProfile.value = getProfileData(userEmail)!!
            //_userWallets.value = getWallets(carteras.keySet())!!
        }
    }

    suspend fun getProfileData(userEmail: String?): Usuario? {
        return try {
            db.collection("Usuarios")
                .whereEqualTo("correo", userEmail)
                .get().await().toUser()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    suspend fun getWallets(w: MutableSet<String>): List<Wallet>? {
        val listaWallets = ArrayList<String>()

        for (clave in w) {
            listaWallets.add(clave)
        }
        return try {
            db.collection("Wallets")
                .whereIn(FieldPath.documentId(), listaWallets)
                .get().await()
                .documents.mapNotNull { it.toWallet() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user friends", e)
            emptyList()
        }
    }


    interface OnDataReceiveCallback {
        fun onDataReceived(user: Usuario)
    }

    suspend fun getFromFirebase(userEmail: String?, callback: OnDataReceiveCallback) {
        db.collection("Usuarios")
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                result.apply {
                    val document = result.first()
                    val user = document.toObject(Usuario::class.java)
                    val parser = JsonParser()
                    val jsonElement = parser.parse(document.get("wallets").toString())
                    val carteras: JsonObject = jsonElement.asJsonObject
                    Log.d("Usuario", carteras.toString())
                    /*viewModelScope.launch {
                        getWalletData(carteras.keySet(), parser)
                    }*/
                    callback.onDataReceived(user)
                }
            }
    }
/*
    suspend fun getWalletsUser(userEmail: String?): Usuario {
        var user = Usuario()
        db.collection("Usuarios")
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                result.apply {
                    val document = result.first()
                    user = document.toObject(Usuario::class.java)
                    val parser = JsonParser()
                    val jsonElement = parser.parse(document.get("wallets").toString())
                    val carteras: JsonObject = jsonElement.asJsonObject
                    //Log.d("Usuario", carteras.toString())
                    viewModelScope.launch {
                        getWalletData(carteras.keySet(), parser)
                    }
                }
            }

        return user
    }

    suspend private fun getWalletData(
        w: MutableSet<String>,
        parser: JsonParser,
    ) {
        val listaWallets = ArrayList<String>()

        for (clave in w) {
            listaWallets.add(clave)
        }

        db.collection("Wallets")
            .whereIn(FieldPath.documentId(), listaWallets)
            .get()
            .addOnSuccessListener { result2 ->
                result2.apply {
                    for (document in result2) {
                        val jsonElement2 = parser.parse(document.get("users").toString())
                        val usuarios: JsonObject = jsonElement2.asJsonObject
                        val usuariosWallet = ArrayList<Usuario>()
                        val listaClaveUsuario = ArrayList<String>()

                        for (claveUsuario in usuarios.keySet()) {
                            listaClaveUsuario.add(claveUsuario)
                        }

                        db.collection("Usuarios")
                            .whereIn(FieldPath.documentId(), listaClaveUsuario)
                            .get()
                            .addOnSuccessListener { result3 ->
                                result3.apply {
                                    for (documentUsuario in result2) {
                                        val user2 = documentUsuario.toObject(Usuario::class.java)
                                        usuariosWallet.add(user2)
                                    }
                                }
                            }

                        val wallet =
                            Wallet(
                                document.get("nombre").toString(),
                                document.get("fecha").toString(),
                                document.get("lugar").toString(),
                                usuarios = usuariosWallet
                            )
                        wallets.add(wallet)
                        Log.d(TAG, wallets.toString())
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }*/
}





