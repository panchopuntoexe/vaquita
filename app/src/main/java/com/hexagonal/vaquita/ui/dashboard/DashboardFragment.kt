package com.hexagonal.vaquita.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hexagonal.vaquita.databinding.FragmentDashboardBinding
import com.hexagonal.vaquita.datos.PAGOS
import com.hexagonal.vaquita.datos.USUARIOS
import com.hexagonal.vaquita.datos.WALLETS
import com.hexagonal.vaquita.entidades.Pago
import com.hexagonal.vaquita.entidades.Wallet
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Column
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.util.ChartUtils


class DashboardFragment : Fragment() {

    private var data: ColumnChartData? = null
    private val hasAxes = true
    private val hasAxesNames = true
    private val hasLabels = true
    private val hasLabelForSelected = false
    private val walletsPagos:MutableList<Pair<String,Number>> = mutableListOf()

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getPagosporWallet()
        return root
    }

    private fun getPagosporWallet() {
        val db = Firebase.firestore
        db.collection(USUARIOS)
            .whereEqualTo("correo", Firebase.auth.currentUser!!.email)
            .get()
            .addOnSuccessListener { result ->
                val user = result.first()
                db.collection(WALLETS)
                    .get()
                    .addOnSuccessListener { result ->
                        val wallets = result.toObjects(Wallet::class.java)
                        val walletsUsuario = mutableListOf<Wallet>()
                        for (wallet in wallets){
                            if(wallet.users!!.keys.contains(user.id)){
                                walletsUsuario.add(wallet)
                            }
                        }
                        db.collection(PAGOS)
                            .get()
                            .addOnSuccessListener { result ->
                            val pagos = result
                                for (wallet in walletsUsuario){
                                    var acumPago = 0
                                    if (wallet.pagos!!.isEmpty()){
                                        walletsPagos.add(Pair(wallet.nombre.toString(),0))
                                    }else{
                                        for(pago in wallet.pagos!!){
                                            for(element in pagos){
                                                if(pago.key==element.id){
                                                    if(element.toObject(Pago::class.java).user==user.id){
                                                        acumPago= (acumPago+element.toObject(Pago::class.java).valor!!).toInt()
                                                    }
                                                }
                                            }
                                        }
                                        walletsPagos.add(Pair(wallet.nombre.toString(),acumPago))
                                    }
                                }
                                graficar()
                            }

                    }
            }
    }

    private fun graficar(){
        val chart = binding.chart
        walletsPagos.size
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns: MutableList<Column> = ArrayList()
        var values: MutableList<SubcolumnValue?>
        for (wallet in walletsPagos) {
            values = ArrayList()
            values.add(
                SubcolumnValue(
                    wallet.second.toFloat(),ChartUtils.pickColor()
                ).setLabel(wallet.first+":"+wallet.second)
            )
            val column = Column(values)
            column.setHasLabels(hasLabels)

            column.setHasLabelsOnlyForSelected(hasLabelForSelected)
            columns.add(column)
        }
        data = ColumnChartData(columns)
        if (hasAxes) {
            val axisX = Axis(emptyList())
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = WALLETS
                axisY.name = PAGOS
            }
            data!!.axisXBottom = axisX
            data!!.axisYLeft = axisY
        } else {
            data!!.axisXBottom = null
            data!!.axisYLeft = null
        }
        chart.columnChartData = data

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}