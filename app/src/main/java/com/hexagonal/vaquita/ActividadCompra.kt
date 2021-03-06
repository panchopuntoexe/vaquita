package com.hexagonal.vaquita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hexagonal.vaquita.adapters.FragmentAdapter
import com.hexagonal.vaquita.entidades.Wallet

class ActividadCompra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_compra)

        val tablayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewpager2)

        val textWalletName2: TextView = findViewById(R.id.textWalletName2)
        val textFecha2: TextView = findViewById(R.id.textFecha2)
        val textLugar2: TextView = findViewById(R.id.textLugar2)

        val intent = intent
        val wallet = intent.getParcelableExtra<Wallet>("wallet")

        textWalletName2.text = wallet?.nombre
        textFecha2.text = wallet?.fecha
        textLugar2.text = wallet?.lugar

        val adapter = FragmentAdapter(
            supportFragmentManager, lifecycle,
            wallet
        )

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

        // Botón  Pagar
        val botonMetodoDePago = findViewById<Button>(R.id.botonPagar)
        botonMetodoDePago.setOnClickListener {
            val pagoUser: TextView = this.findViewById(R.id.pagoUser)
            val deuda = pagoUser.text.toString().toDouble()
            val intencion = Intent(this, ActividadMetodoDePago::class.java)
            intencion.putExtra("deuda",deuda)
            intencion.putExtra("wallet",wallet)
            startActivity(intencion)
        }

        // Botón Gasto
        val botonGasto = findViewById<Button>(R.id.botonGasto)
        botonGasto.setOnClickListener {
            val intencion = Intent(this, ActivityAgregarGasto::class.java)
            intencion.putExtra("wallet",wallet)
            startActivity(intencion)
        }


    }

    override fun onBackPressed() {
        val intencion = Intent(this, ActividadHome::class.java)
        startActivity(intencion)
        this.finish()
    }

}