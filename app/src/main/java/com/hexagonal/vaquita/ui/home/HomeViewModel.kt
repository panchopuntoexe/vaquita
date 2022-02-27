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
}





