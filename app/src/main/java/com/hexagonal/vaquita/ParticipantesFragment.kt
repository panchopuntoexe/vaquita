package com.hexagonal.vaquita

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.hexagonal.vaquita.adapters.GastoAdapter
import com.hexagonal.vaquita.adapters.ParticipanteAdapter
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Usuario.Companion.toUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
    val participantes: Map<String, Boolean>?,
    val onWalletListener: GastoAdapter.OnWalletListener
) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    val TAG = "ErrorUsuario"

    val _userWallets = MutableLiveData<List<Usuario>>()
    var usuarios: LiveData<List<Usuario>>? = _userWallets


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            _userWallets.value = getWallets(participantes)!!
        }
    }

    suspend fun getWallets(participantes: Map<String, Boolean>?): List<Usuario>? {
        val listaWallets = ArrayList<String>()

        if (participantes != null) {
            for (clave in participantes.keys) {
                listaWallets.add(clave)
            }
        }
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_participantes, container, false)
        val recycleViewParticipantes: RecyclerView =
            view.findViewById(R.id.recycleViewParticipantes)

        usuarios?.observe(viewLifecycleOwner, Observer {
            recycleViewParticipantes?.adapter =
                ParticipanteAdapter(this.requireActivity(), it, 10.25, onWalletListener)
            recycleViewParticipantes?.layoutManager =
                LinearLayoutManager(this.requireActivity())
            recycleViewParticipantes?.setHasFixedSize(true)
        })


        // Inflate the layout for this fragment
        return view
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


