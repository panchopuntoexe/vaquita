package com.hexagonal.vaquita

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.adapters.GastoAdapter
import com.hexagonal.vaquita.adapters.ParticipanteAdapter
import com.hexagonal.vaquita.entidades.Gasto
import com.hexagonal.vaquita.entidades.Gasto.Companion.toGasto
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUser
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
    val gastos: Map<String, Boolean>?,
    val onWalletListener: GastoAdapter.OnWalletListener
) : Fragment() {

    val db = Firebase.firestore
    val TAG = "ErrorUsuario"
    val _gastosWallets = MutableLiveData<List<Gasto>>()
    var gastosWallets: LiveData<List<Gasto>>? = _gastosWallets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    suspend fun getGastos(): List<Gasto>? {
        val listaWallets = ArrayList<String>()

        if (gastos != null) {
            for (clave in gastos.keys) {
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_gastos, container, false)
        val recycleViewGastos: RecyclerView =
            view.findViewById(R.id.recycleViewGastos)
        viewLifecycleOwner.lifecycleScope.launch {

            _gastosWallets.value = getGastos()!!

        }

        gastosWallets?.observe(viewLifecycleOwner, Observer {
            recycleViewGastos.adapter =
                GastoAdapter(this.requireActivity(), it, onWalletListener)
            recycleViewGastos.layoutManager =
                LinearLayoutManager(this.requireActivity())
            recycleViewGastos.setHasFixedSize(true)
        })
        return view
    }
}