package com.hexagonal.vaquita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hexagonal.vaquita.adapters.GastoAdapter
import com.hexagonal.vaquita.adapters.WalletAdapter
import com.hexagonal.vaquita.entidades.Wallet

class ActividadCompra : AppCompatActivity(), GastoAdapter.OnWalletListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_compra)

        val tablayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewpager2)

        val adapter = FragmentAdapter(supportFragmentManager, lifecycle, this)
        viewPager2.adapter = adapter
        TabLayoutMediator(tablayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "gastos"
                }
                1 -> {
                    tab.text = "Participantes"
                }
            }
        }.attach()


        // boton  método de pago
        var botonMetodoDePago = findViewById<Button>(R.id.botonPagar)
        botonMetodoDePago.setOnClickListener {
            val intencion = Intent(this, ActividadMetodoDePago::class.java)
            startActivity(intencion)
        }

        // boton registro
        var botonGasto = findViewById<Button>(R.id.botonGasto)
        botonGasto.setOnClickListener{
            val intencion = Intent(this,ActivityAgregarGasto::class.java)
            startActivity(intencion)
        }

        val intent = getIntent()
        val wallet = intent.getParcelableExtra<Wallet>("wallet")
        Log.d("UsuarioW", wallet.toString())
    }

    override fun onWalletClick(position: Int) {
        TODO("Not yet implemented")
    }
}