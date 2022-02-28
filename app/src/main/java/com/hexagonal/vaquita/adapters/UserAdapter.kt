package com.hexagonal.vaquita.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import com.squareup.picasso.Picasso

class UserAdapter(context: Context, usuarios: ArrayList<Usuario>,wallet: String, layout: Int) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
        private val context: Context
        private val usuarios: ArrayList<Usuario>
        init {
            this.context = context
            this.usuarios = usuarios
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.contactos_layout, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                Picasso.get().load(usuarios[position].foto).fit()
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.image)
                holder.txtNombre.text = usuarios[position].nombre
                holder.txtCorreo.text = usuarios[position].correo
                holder.button.setOnClickListener(){
                    button.isEnabled=false
                    var db = Firebase.firestore
                    db.collection("Wallets").document("GXVNAQX2kJsWTtUb3QFa").
                    get().addOnSuccessListener { result->
                        var wallet = result.toObject(Wallet::class.java)
                        db.collection("Usuarios").whereEqualTo("correo",usuarios[position].correo)
                            .get().addOnSuccessListener{result ->
                                var document = result.first()
                                Log.d("ID",document.id.toString())
                        }
                    }
                }
            }

        }

        override fun getItemCount(): Int {
            return usuarios.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var image: ImageView
            var txtNombre: TextView
            var txtCorreo: TextView
            var button: Button
            init {
                image = itemView.findViewById(R.id.fotoUsuario) as ImageView
                txtNombre = itemView.findViewById(R.id.textViewNombreUsuario)
                txtCorreo = itemView.findViewById(R.id.textViewCorreo)
                button = itemView.findViewById(R.id.buttonAñadirUsuario)

            }
        }
    }