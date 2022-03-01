package com.hexagonal.vaquita.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.databinding.FragmentDashboardBinding
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Usuario
import com.hexagonal.vaquita.entidades.Wallet
import lecho.lib.hellocharts.model.*

import lecho.lib.hellocharts.model.ColumnChartData

import lecho.lib.hellocharts.util.ChartUtils

import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.view.ColumnChartView
import lecho.lib.hellocharts.view.LineChartView


class DashboardFragment : Fragment() {

    private val DEFAULT_DATA = 0
    private val SUBCOLUMNS_DATA = 1
    private val STACKED_DATA = 2
    private val NEGATIVE_SUBCOLUMNS_DATA = 3
    private val NEGATIVE_STACKED_DATA = 4

    private var data: ColumnChartData? = null
    private val hasAxes = true
    private val hasAxesNames = true
    private val hasLabels = true
    private val hasLabelForSelected = false
    private val walletsPagos:MutableMap<String,Number> = mutableMapOf()

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root



        getUser()




        val chart = binding.chart
        val numSubcolumns = 1
        val numColumns = 12
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns: MutableList<Column> = ArrayList()
        var values: MutableList<SubcolumnValue?>
        for (i in 0 until numColumns) {
            values = ArrayList()
            for (j in 0 until numSubcolumns) {
                values.add(
                    SubcolumnValue(
                        Math.random().toFloat() * 50f + 5,
                        ChartUtils.pickColor()
                    )
                )
            }
            val column = Column(values)
            column.setHasLabels(hasLabels)

            column.setHasLabelsOnlyForSelected(hasLabelForSelected)
            columns.add(column)
        }
        data = ColumnChartData(columns)
        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Axis X"
                axisY.name = "Axis Y"
            }
            data!!.setAxisXBottom(axisX)
            data!!.setAxisYLeft(axisY)
        } else {
            data!!.setAxisXBottom(null)
            data!!.setAxisYLeft(null)
        }
        chart.setColumnChartData(data)
        return root
    }

    fun getUser() {
        val db = Firebase.firestore
        var userfb : Usuario
        db.collection("Usuarios")
            .whereEqualTo("correo", Firebase.auth.currentUser!!.email)
            .get()
            .addOnSuccessListener { result ->
                val user = result.first()
                db.collection("Wallets")
                    .get()
                    .addOnSuccessListener { result ->
                        var wallets = result.toObjects(Wallet::class.java)
                        var walletsUsuario = mutableListOf<Wallet>()
                        for (wallet in wallets){
                            if(wallet.users!!.keys.contains(user.id)){
                                walletsUsuario.add(wallet)
                            }
                        }
                        for (wallet in walletsUsuario){
                            var acumPago = 0
                            if (wallet.pagos!!.isEmpty()){
                                walletsPagos.put(wallet.nombre.toString(),0)
                            }else{
                                for(pago in wallet.pagos!!){
                                    db.collection("Pagos")
                                        .document(pago.key)
                                        .get()
                                        .addOnSuccessListener { result ->
                                            if((result.toObject(Pago::class.java))!!.user===user.id){
                                                acumPago= (acumPago+ result.toObject(Pago::class.java)!!.valor!!).toInt()
                                            }
                                        }
                                }
                                walletsPagos.put(wallet.nombre.toString(),acumPago)
                            }
                        }
                        Log.d("Wallets",walletsUsuario.toString())
                        Log.d("Wallets",walletsPagos.toString())
                    }
            }




    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}