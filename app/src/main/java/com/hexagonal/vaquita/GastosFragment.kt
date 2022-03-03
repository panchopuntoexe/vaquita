package com.hexagonal.vaquita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.adapters.GastoAdapter
import com.hexagonal.vaquita.datos.GASTOS
import com.hexagonal.vaquita.datos.PAGOS
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.entidades.Gasto
import com.hexagonal.vaquita.entidades.Gasto.Companion.toGasto
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Pago.Companion.toPago
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUserId
import com.hexagonal.vaquita.entidades.Wallet
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class GastosFragment(
    val wallet: Wallet?,
) : Fragment() {

    val db = Firebase.firestore
    private val _gastosWallets = MutableLiveData<List<Gasto>>()
    private var gastosWallets: LiveData<List<Gasto>>? = _gastosWallets
    private val _pagosWallets = MutableLiveData<List<Pago>>()
    private var pagosWallets: LiveData<List<Pago>>? = _pagosWallets

    private val _userId = MutableLiveData<String>()
    private var userId: LiveData<String>? = _userId

    private suspend fun getGastos(): List<Gasto>? {
        val listaWallets = ArrayList<String>()

        if (wallet?.gastos != null) {
            for (clave in wallet.gastos!!.keys) {
                listaWallets.add(clave)
            }
        }

        if (listaWallets.size > 0) {
            return try {
                db.collection(GASTOS)
                    .whereIn(FieldPath.documentId(), listaWallets)
                    .get().await()
                    .documents.mapNotNull { it.toGasto() }
            } catch (e: Exception) {
                emptyList()
            }
        }
        return emptyList()
    }

    private suspend fun getUserID(): String? {
        val userEmail: String? = Firebase.auth.currentUser?.email
        return try {
            db.collection(USUARIOS)
                .whereEqualTo("correo", userEmail)
                .get()
                .await().toUserId()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getPagos(): List<Pago>? {
        val listaWallets = ArrayList<String>()

        if (wallet?.pagos != null) {
            for (clave in wallet.pagos!!.keys) {
                listaWallets.add(clave)
            }
        }

        if (listaWallets.size > 0) {
            return try {
                db.collection(PAGOS)
                    .whereIn(FieldPath.documentId(), listaWallets)
                    .get().await()
                    .documents.mapNotNull { it.toPago() }
            } catch (e: Exception) {
                emptyList()
            }
        }
        return emptyList()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_gastos, container, false)
        val recycleViewGastos: RecyclerView =
            view.findViewById(R.id.recycleViewGastos)
        viewLifecycleOwner.lifecycleScope.launch {
            _gastosWallets.value = getGastos()!!
            _pagosWallets.value = getPagos()!!
            _userId.value = getUserID()!!
        }

        gastosWallets?.observe(viewLifecycleOwner, Observer { listaGastos ->

            var gastos: ArrayList<Gasto> = ArrayList()
            if (listaGastos.isNotEmpty()) {
                gastos = listaGastos as ArrayList<Gasto>
            }

            var total = 0.0
            for (gasto in listaGastos) {
                total += gasto.valor!!
            }

            val valorTotalWallet: TextView =
                this.requireActivity().findViewById(R.id.valorTotalWallet)
            valorTotalWallet.text = total.toString()
            pagosWallets?.observe(viewLifecycleOwner, Observer { listaPagos ->
                //val gastosCompletos = merge(gastos, pagos)
                var totalPago = 0.0
                var totalPagoUser = 0.0

                userId?.observe(viewLifecycleOwner, Observer {
                    for (pago in listaPagos) {
                        totalPago += pago.valor!!
                        if (pago.user == it) {
                            totalPagoUser += pago.valor!!
                        }
                        gastos.add(pago.pagoToGasto())
                    }

                    val valorTotalPagos: TextView =
                        this.requireActivity().findViewById(R.id.valorTotalPagos)
                    valorTotalPagos.text = totalPago.toString()

                    recycleViewGastos.adapter =
                        GastoAdapter(gastos)
                    recycleViewGastos.layoutManager =
                        LinearLayoutManager(this.requireActivity())
                    recycleViewGastos.setHasFixedSize(true)
                })
            })
        })
        return view
    }


    fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first + second
    }
}