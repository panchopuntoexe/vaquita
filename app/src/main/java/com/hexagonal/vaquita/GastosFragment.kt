package com.hexagonal.vaquita

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.hexagonal.vaquita.adapters.ParticipanteAdapter
import com.hexagonal.vaquita.entidades.Gasto
import com.hexagonal.vaquita.entidades.Gasto.Companion.toGasto
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Pago.Companion.toPago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUser
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUserId
import com.hexagonal.vaquita.entidades.Wallet
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GastosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GastosFragment(
    val wallet: Wallet?,
) : Fragment() {

    val db = Firebase.firestore
    val TAG = "ErrorUsuario"
    val _gastosWallets = MutableLiveData<List<Gasto>>()
    var gastosWallets: LiveData<List<Gasto>>? = _gastosWallets
    val _pagosWallets = MutableLiveData<List<Pago>>()
    var pagosWallets: LiveData<List<Pago>>? = _pagosWallets

    val _userId = MutableLiveData<String>()
    var userId: LiveData<String>? = _userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun getGastos(): List<Gasto>? {
        val listaWallets = ArrayList<String>()

        if (wallet?.gastos != null) {
            for (clave in wallet.gastos!!.keys) {
                listaWallets.add(clave)
            }
        }

        if (listaWallets.size > 0) {
            Log.d("Participantes", listaWallets.toString())
            return try {
                db.collection("Gastos")
                    .whereIn(FieldPath.documentId(), listaWallets)
                    .get().await()
                    .documents.mapNotNull { it.toGasto() }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting user friends", e)
                emptyList()
            }
        }
        return emptyList()
    }

    suspend fun getUserID(): String? {
        val userEmail: String? = Firebase.auth.currentUser?.email
        return try {
            db.collection("Usuarios")
                .whereEqualTo("correo", userEmail)
                .get()
                .await().toUserId()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    suspend fun getPagos(): List<Pago>? {
        val listaWallets = ArrayList<String>()

        if (wallet?.pagos != null) {
            for (clave in wallet.pagos!!.keys) {
                listaWallets.add(clave)
            }
        }

        if (listaWallets.size > 0) {
            Log.d("Participantes", listaWallets.toString())
            return try {
                db.collection("Pagos")
                    .whereIn(FieldPath.documentId(), listaWallets)
                    .get().await()
                    .documents.mapNotNull { it.toPago() }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting user friends", e)
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

        gastosWallets?.observe(viewLifecycleOwner, Observer {
            val gastos: ArrayList<Gasto> = it as ArrayList<Gasto>
            var total = 0.0
            for (gasto in it) {
                total += gasto.valor!!
            }
            Log.d("Total gasto", total.toString())
            val valorTotalWallet: TextView =
                this.requireActivity().findViewById(R.id.valorTotalWallet)
            valorTotalWallet.setText(total.toString())
            pagosWallets?.observe(viewLifecycleOwner, Observer {
                //val gastosCompletos = merge(gastos, pagos)
                val pagos = it
                var totalPago = 0.0
                var totalPagoUser = 0.0

                userId?.observe(viewLifecycleOwner, Observer {
                    for (pago in pagos) {
                        totalPago += pago.valor!!
                        if (pago.user == it) {
                            totalPagoUser += pago.valor!!
                        }
                        gastos.add(pago.pagoToGasto())
                    }

                    val valorTotalPagos: TextView =
                        this.requireActivity().findViewById(R.id.valorTotalPagos)
                    valorTotalPagos.setText(totalPago.toString())


                    Log.d("Total pago", totalPago.toString())
                    Log.d("Total pago usuario", totalPagoUser.toString())

                    recycleViewGastos.adapter =
                        GastoAdapter(this.requireActivity(), gastos)
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