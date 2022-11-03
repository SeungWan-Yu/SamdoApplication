package com.smarthive.samdoapplication.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarthive.samdoapplication.Activity.SensorDetailActivity
import com.smarthive.samdoapplication.Activity.SensorPopupActivity
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.adapter.SensorListAdapter
import com.smarthive.samdoapplication.databinding.FragmentSensorBinding
import com.smarthive.samdoapplication.model.SensorData
import com.smarthive.samdoapplication.model.SensorModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SensorFragment : Fragment() {

    private lateinit var binding: FragmentSensorBinding
    var SensorData = ArrayList<SensorData>()
    lateinit var resultListner : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultListner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                fromSub ->
            when(fromSub.resultCode){
                0 -> { //성공
                    Toast.makeText(requireContext(), "등록 성공", Toast.LENGTH_SHORT).show()
                    getSensor()
                }
                1 -> { //실패 - 데이터 오류
                    Toast.makeText(requireContext(), "등록 실패", Toast.LENGTH_SHORT).show()
                }
                2 -> { // 실패 - 통신 오류
                    Toast.makeText(requireContext(), "통신 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sensor, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSensor()

        binding.sfrefreshLayout.setOnRefreshListener {
            getSensor()
            binding.sfrefreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
        }

        binding.sensorfB.setOnClickListener {
            val nIntent = Intent(context, SensorPopupActivity::class.java)
            resultListner.launch(nIntent)
        }
    }

    private fun getSensor() {
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = LoadingDialog(requireContext())
            dialog.show()
            App.retrofitService.getsensorlist().enqueue(object : Callback<SensorModel> {
                override fun onResponse(call: Call<SensorModel>, response: Response<SensorModel>) {
                    val body = response.body()
                    if (body != null) {
                        SensorData = body.data as ArrayList<SensorData>
                        setAdapter(SensorData)
                    }
                    dialog.dismiss()
                }
                override fun onFailure(call: Call<SensorModel>, t: Throwable) {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "통신 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setAdapter(sensorlist: List<SensorData>) {
        val sensordapter = SensorListAdapter(requireContext(),sensorlist) {sensor ->
            comItemClicked(sensor)
        }
        binding.sensorrecyclerView.adapter = sensordapter
        binding.sensorrecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sensorrecyclerView.setHasFixedSize(false)
    }

    private fun comItemClicked(device: SensorData) {
        val nIntent = Intent(requireContext(), SensorDetailActivity::class.java)
        if (device != null) {
            nIntent.putExtra("sensorname", device.sensorname)
            nIntent.putExtra("port", device.port)
        }
//        nIntent.putExtra("SN", bee?.EQPMN_ESNTL_SN)
        startActivity(nIntent)
    }


}