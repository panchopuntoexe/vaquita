package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.adapters.UserAdapter
import com.hexagonal.vaquita.databinding.ActivityActividadContactosBinding
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet


class ActividadContactos : AppCompatActivity() {

    private var usuarios = ArrayList<Usuario>()
    private lateinit var botonContinuar: Button
    private lateinit var barraBusqueda: SearchView
    private lateinit var binding: ActivityActividadContactosBinding
    private lateinit var userauth: FirebaseUser
    private lateinit var mailDeUsuarioLogueado: String
    private lateinit var walletId: String
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_contactos)
        // boton guardar Contactos
        binding = ActivityActividadContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        botonContinuar = findViewById<Button>(R.id.botonContinuarWallet)
        barraBusqueda = findViewById(R.id.barraBuscar)
        adapter = UserAdapter(
            this,
            usuarios,
            intent.extras!!.getString("walletId").toString(),
            R.layout.contactos_layout
        )

        walletId = intent.extras!!.getString("walletId")!!

        getUsuarios()


        botonContinuar.setOnClickListener() {
            val intencion = Intent(this, ActividadCompra::class.java)
            Firebase.firestore.collection("Wallets")
                .document(walletId).get()
                .addOnSuccessListener { result ->
                    intencion.putExtra("wallet", result.toObject(Wallet::class.java))
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(R.string.exitoWallet)
                        .setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                startActivity(intencion)
                            })
                    builder.create()
                    builder.show()
                }
        }

        barraBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                adapter = UserAdapter(
                    this@ActividadContactos,
                    usuarios.filter{ usuario-> usuario.nombre!!.lowercase().contains(p0.toString().lowercase())} as ArrayList<Usuario>,
                    intent.extras!!.getString("walletId").toString(),
                    R.layout.contactos_layout
                )
                binding.recyclerView.adapter = adapter
                return false
            }
        })

        //datos del usuario logueado
        userauth = Firebase.auth.currentUser!!
        mailDeUsuarioLogueado = userauth.email.toString()

        putCreadorComoParticipante()
    }

    fun putCreadorComoParticipante() {
        var db = Firebase.firestore
        db.collection("Usuarios").whereEqualTo("correo", mailDeUsuarioLogueado)
            .get().addOnSuccessListener { result ->
                var userAux = result.first().toObject(Usuario::class.java).wallets as MutableMap<String, Boolean>
                userAux.put(walletId,true)
                db.collection("Usuarios").document(result.first().id)
                                                    .update("wallets", userAux)
            }
    }

    fun getUsuarios() {
        var db = Firebase.firestore
        db.collection("Usuarios").get().addOnSuccessListener { result ->
            for (document in result) {
                //verifico que no sea el usuario logueado
                if (!document.toObject(Usuario::class.java).correo.equals(mailDeUsuarioLogueado)) {
                    usuarios.add(document.toObject(Usuario::class.java))
                }
            }
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onBackPressed() {}
}