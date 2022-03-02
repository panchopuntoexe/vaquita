package com.hexagonal.vaquita

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.adapters.ParticipanteAdapter
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Pago.Companion.toPago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Usuario.Companion.toMapUser
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUser
import com.hexagonal.vaquita.entidades.Wallet
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.pow
import kotlin.math.roundToInt


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_RECYCLEVIEW = "recycleViewParticipantes"

/**
 * A simple [Fragment] subclass.
 * Use the [ParticipantesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParticipantesFragment(
    val wallet: Wallet?,
) : Fragment() {

    val db = Firebase.firestore
    val TAG = "ErrorUsuario"
    val _userWallets = MutableLiveData<List<Usuario>>()
    var usuarios: LiveData<List<Usuario>>? = _userWallets

    val _pagosWallets = MutableLiveData<List<Pago>>()
    var pagosWallets: LiveData<List<Pago>>? = _pagosWallets

    val _usersCorreo = MutableLiveData<List<MutableMap<String, String>>>()
    var usersCorreo: LiveData<List<MutableMap<String, String>>>? = _usersCorreo

    val mapaPagos: MutableMap<String?, Double> = mutableMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun getUsers(): List<Usuario>? {
        val listaWallets = ArrayList<String>()

        if (wallet?.users != null) {
            for (clave in wallet.users!!.keys) {
                listaWallets.add(clave)
            }
        }
        Log.d("Participantes", listaWallets.toString())
        return try {
            db.collection("Usuarios")
                .whereIn(FieldPath.documentId(), listaWallets)
                .get().await()
                .documents.mapNotNull { it.toUser() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user friends", e)
            emptyList()
        }
    }

    suspend fun getUsersMap(lista: MutableSet<String?>): List<MutableMap<String, String>>? {
        val listaWallets = ArrayList<String>()
        for (clave in lista) {
            if (clave != null) {
                listaWallets.add(clave)
            }
        }

        if (listaWallets.size > 0) {
            Log.d("Participantes", listaWallets.toString())
            return try {
                db.collection("Usuarios")
                    .whereIn(FieldPath.documentId(), listaWallets)
                    .get().await()
                    .documents.mapNotNull { it.toMapUser() }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting user friends", e)
                emptyList()
            }
        }

        return emptyList()
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
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_participantes, container, false)
        val recycleViewParticipantes: RecyclerView =
            view.findViewById(R.id.recycleViewParticipantes)
        viewLifecycleOwner.lifecycleScope.launch {
            _userWallets.value = getUsers()!!
            _pagosWallets.value = getPagos()!!
        }
        usuarios?.observe(viewLifecycleOwner, Observer {
            try {
                val articipantesWallets = it
                val numParticipantes = it.size
                val valorTotalWallet: TextView =
                    this.requireActivity().findViewById(R.id.valorTotalWallet)
                val gastoTotal = valorTotalWallet.text.toString().toDouble()
                val deuda = (gastoTotal / numParticipantes).roundTo(2)



                pagosWallets?.observe(viewLifecycleOwner, Observer { it ->
                    val pagos = it
                    val mutableByUser: MutableMap<String?, MutableList<Pago>> =
                        pagos.groupByTo(mutableMapOf()) { it.user }

                    val mapaPagos: MutableMap<String?, Double> = mutableMapOf()
                    for (m in mutableByUser) {
                        var totalPago = 0.0
                        for (pago in m.value) {
                            totalPago += pago.valor!!
                        }
                        mapaPagos.put(m.key, totalPago)
                    }

                    viewLifecycleOwner.lifecycleScope.launch {
                        _usersCorreo.value = getUsersMap(mapaPagos.keys)!!
                    }

                    usersCorreo?.observe(viewLifecycleOwner, Observer { lista ->
                        val mapaPagosNuevo: MutableMap<String?, Double> = mutableMapOf()
                        for (mPago in mapaPagos) {
                            for (mapa in lista) {
                                if (mPago.key == mapa.keys.first()) {
                                    mapaPagosNuevo.put(mapa.values.first(), mPago.value)
                                }
                            }
                        }

                        recycleViewParticipantes.adapter =
                            ParticipanteAdapter(
                                this.requireActivity(),
                                articipantesWallets,
                                deuda,
                                mapaPagosNuevo
                            )
                        recycleViewParticipantes.layoutManager =
                            LinearLayoutManager(this.requireActivity())
                        recycleViewParticipantes.setHasFixedSize(true)

                    })


                })
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        })

        // Inflate the layout for this fragment
        return view
    }


    fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParticipantesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParticipantesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}


