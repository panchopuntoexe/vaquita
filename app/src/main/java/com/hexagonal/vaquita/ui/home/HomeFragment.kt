package com.hexagonal.vaquita.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hexagonal.vaquita.ActividadCompra
import com.hexagonal.vaquita.adapters.WalletAdapter
import com.hexagonal.vaquita.databinding.FragmentHomeBinding
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.hexagonal.vaquita.ui.settings.SettingsFragment


class HomeFragment : Fragment(), WalletAdapter.OnWalletListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var wallets = ArrayList<Wallet>()
    private lateinit var userActual: Usuario
    private lateinit var perfil: ImageView
    private lateinit var textBienvenida: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.recycleViewWallets.addItemDecoration(
            DividerItemDecoration(
                binding.recycleViewWallets.context,
                DividerItemDecoration.VERTICAL
            )
        )


        try {
            val userEmail: String? = Firebase.auth.currentUser?.email
            getWalletsUser(userEmail)
            Log.d("Email", userEmail.toString())
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }

        /*val wallets = arrayListOf<Wallet>(
            Wallet("Comic Listening Party1", "08/01/2022"),
            Wallet("Comic Listening Party2", "08/01/2022"),
            Wallet("Comic Listening Party3", "08/01/2022"),
            Wallet("Comic Listening Party4", "08/01/2022"),
            Wallet("Comic Listening Party5", "08/01/2022"),
            Wallet("Comic Listening Party6", "08/01/2022"),
            Wallet("Comic Listening Party7", "08/01/2022"),
        )*/


        //getWallets()

        /*val cardView1 = binding.cardView1
        cardView1.setOnClickListener {
            val intencion = Intent(this.activity, ActividadCompra::class.java)
            startActivity(intencion)
        }*/

        perfil = binding.imageProfile
        textBienvenida = binding.textBienvenida


        perfil.setOnClickListener {
            val fragmentB = SettingsFragment()
            requireFragmentManager().beginTransaction()
                .replace(com.hexagonal.vaquita.R.id.nav_host_fragment_activity_home, fragmentB)
                .addToBackStack(this::class.java.simpleName)
                .commit()
        }


        return root
    }

    fun getWalletsUser(userEmail: String?) {
        val db = Firebase.firestore

        val TAG = "Wallets"
        db.collection("Usuarios")
            .whereEqualTo("correo", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val document = result.first()
                val user = document.toObject(Usuario::class.java)
                val parser = JsonParser()
                val jsonElement = parser.parse(document.get("wallets").toString())
                val carteras: JsonObject = jsonElement.asJsonObject
                Log.d("Usuario", carteras.toString())
                for (w in carteras.keySet()) {
                    getWalletData(db, w, parser, TAG)
                }
                userActual = user
                textBienvenida.setText("Bienvenido ${userActual.nombre}")
                Glide.with(this).load(userActual.foto).into(perfil)
                setWalletToRecycle()
            }
    }

    private fun getWalletData(
        db: FirebaseFirestore,
        w: String,
        parser: JsonParser,
        TAG: String
    ) {
        db.collection("Wallets").document(w).get()
            .addOnSuccessListener { result2 ->
                val jsonElement2 = parser.parse(result2.get("users").toString())
                val usuarios: JsonObject = jsonElement2.asJsonObject
                val usuariosWallet = ArrayList<Usuario>()
                for (u in usuarios.keySet()) {
                    getUserData(db, u, usuariosWallet)
                }
                val wallet =
                    Wallet(
                        result2.get("nombre").toString(),
                        result2.get("fecha").toString(),
                        result2.get("lugar").toString(),
                        usuarios = usuariosWallet
                    )
                wallets.add(wallet)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun getUserData(
        db: FirebaseFirestore,
        u: String,
        usuariosWallet: ArrayList<Usuario>
    ) {
        db.collection("Usuarios").document(u).get()
            .addOnSuccessListener { result3 ->
                val user2 = result3.toObject(Usuario::class.java)
                if (user2 != null) {
                    usuariosWallet.add(user2)
                }
            }
    }

    private fun setWalletToRecycle() {
        Log.w("wawalets", wallets.toString())
        binding.recycleViewWallets.adapter =
            WalletAdapter(this.requireActivity(), wallets, 10.25, this)
        binding.recycleViewWallets.layoutManager =
            LinearLayoutManager(this.requireActivity())
        binding.recycleViewWallets.setHasFixedSize(true)
    }

    /*fun getWallets() {
        val TAG = "Wallets"
        val db = Firebase.firestore
        db.collection("Wallets")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val parser = JsonParser()
                    val jsonElement = parser.parse(document.get("users").toString())
                    val usuarios: JsonObject = jsonElement.getAsJsonObject()
                    var usuariosWallet = ArrayList<Usuario>()
                    for (u in usuarios.keySet()) {
                        db.collection("Usuarios").document(u).get()
                            .addOnSuccessListener { result ->
                                val user = result.toObject(Usuario::class.java)
                                if (user != null) {
                                    usuariosWallet.add(user)
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
                }

                binding.recycleViewWallets.adapter =
                    WalletAdapter(this.requireActivity(), wallets, 10.25, this)
                binding.recycleViewWallets.layoutManager =
                    LinearLayoutManager(this.requireActivity())
                binding.recycleViewWallets.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWalletClick(position: Int) {
        val wallet = wallets.get(position)
        val intencion = Intent(this.requireActivity(), ActividadCompra::class.java)

        try {
            intencion.putExtra("wallet", wallet)
            startActivity(intencion)
            Log.d("todobien", wallet.toString())
        } catch (e: Exception) {
            Log.d("ErrorWallet", e.toString())
        }
    }
}