package com.hexagonal.vaquita.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.R
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.datos.WALLETS
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet

class UserAdapter(context: Context, usuarios: ArrayList<Usuario>, walletId: String) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val context: Context = context
    private val usuarios: ArrayList<Usuario> = usuarios
    private val walletId: String = walletId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contactos_layout, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            Glide.with(context).load(usuarios[position].foto).into(holder.image)
            holder.txtNombre.text = usuarios[position].nombre
            if(usuarios[position].correo!!.length>22){
                holder.txtCorreo.text = usuarios[position].correo!!.substring(0,22)+"..."
            }else{
                holder.txtCorreo.text = usuarios[position].correo
            }
            Firebase.firestore.collection(USUARIOS).whereEqualTo("correo",usuarios[position].correo)
                .get().addOnSuccessListener {result ->
                if(result.first().toObject(Usuario::class.java).wallets!!.keys.contains(walletId)){
                    button.isEnabled=false
                }

            }


            holder.button.setOnClickListener {
                button.isEnabled = false
                val db = Firebase.firestore
                db.collection(WALLETS).document(walletId).get().addOnSuccessListener { result ->
                    val wallet = result.toObject(Wallet::class.java)
                    db.collection(USUARIOS).whereEqualTo("correo", usuarios[position].correo)
                        .get().addOnSuccessListener { result ->
                            val document = result.first()
                            var userAux: MutableMap<String, Boolean> = wallet!!.users as MutableMap<String, Boolean>
                            userAux[document.id] = true
                            db.collection(WALLETS).document(walletId).update("users", userAux)

                            //poner id de wallet en usuario
                            userAux = document.toObject(Usuario::class.java).wallets as MutableMap<String, Boolean>
                            userAux[walletId] = true
                            db.collection(USUARIOS).document(document.id)
                                                    .update(WALLETS, userAux)
                        }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.fotoUsuario) as ImageView
        var txtNombre: TextView = itemView.findViewById(R.id.textViewNombreUsuario)
        var txtCorreo: TextView = itemView.findViewById(R.id.textViewCorreo)
        var button: Button = itemView.findViewById(R.id.buttonAñadirUsuario)

    }
}