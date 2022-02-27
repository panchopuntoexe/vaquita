package com.hexagonal.vaquita.ui.pay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
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

class PayViewModel : ViewModel() {

    val TAG = "Wallets"
    val db = Firebase.firestore
    private val _userProfile = MutableLiveData<Usuario>()
    val _userWallets = MutableLiveData<List<Wallet>>()
    var userActual: LiveData<Usuario>? = _userProfile
    var wallets: LiveData<List<Wallet>>? = _userWallets

    init {
        viewModelScope.launch {
            val userEmail: String? = Firebase.auth.currentUser?.email
            _userProfile.value = getProfileData(userEmail)!!
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
}