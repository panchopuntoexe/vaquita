package com.hexagonal.vaquita

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.adapters.UserAdapter
import com.hexagonal.vaquita.databinding.ActivityActividadContactosBinding
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet


class ActividadContactos : AppCompatActivity() {

    private var usuarios=ArrayList<Usuario>()
    private lateinit var botonContinuar:Button
    private lateinit var binding:ActivityActividadContactosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_contactos)
        // boton guardar Contactos
        binding= ActivityActividadContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        botonContinuar= findViewById<Button>(R.id.botonContinuarWallet)

        getUsuarios()


        botonContinuar.setOnClickListener(){
            val intencion = Intent(this,ActividadCompra::class.java)
            Firebase.firestore.collection("Wallets")
                .document(intent.extras!!.getString("walletId")!!).get()
                .addOnSuccessListener { result ->
                    intencion.putExtra("wallet",result.toObject(Wallet::class.java))
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


    }

    fun getUsuarios(){
        var db = Firebase.firestore
        db.collection("Usuarios").get().addOnSuccessListener { result ->
            for (document in result) {
                usuarios.add(document.toObject(Usuario::class.java))
            }
            binding.recyclerView.adapter = UserAdapter(this, usuarios, intent.extras!!.getString("walletId").toString(),R.layout.contactos_layout)
        }
    }
}