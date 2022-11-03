package com.smarthive.samdoapplication.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarthive.samdoapplication.Activity.PlasmaDetailActivity
import com.smarthive.samdoapplication.Activity.PlasmaPopupActivity
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.adapter.DeviceListAdapter
import com.smarthive.samdoapplication.databinding.FragmentPlasmaBinding
import com.smarthive.samdoapplication.model.DeviceData
import com.smarthive.samdoapplication.model.DeviceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlasmaFragment : Fragment() {

    private lateinit var binding : FragmentPlasmaBinding
    var DeviceData = ArrayList<DeviceData>()
    lateinit var resultListner : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultListner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            fromSub ->
            when(fromSub.resultCode){
                0 -> { //성공
                    Toast.makeText(requireContext(), "등록 성공", Toast.LENGTH_SHORT).show()
                    getDevice()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_plasma, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDevice()

        binding.pfrefreshLayout.setOnRefreshListener {
            getDevice()
            binding.pfrefreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
        }
        binding.plasmafB.setOnClickListener {
            val nIntent = Intent(context, PlasmaPopupActivity::class.java)
            resultListner.launch(nIntent)
        }
    }



    private fun getDevice(){
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = LoadingDialog(requireContext())
            dialog.show()
            App.retrofitService.getdevicelist().enqueue(object : Callback<DeviceModel> {
                override fun onResponse(call: Call<DeviceModel>, response: Response<DeviceModel>) {
                    val body = response.body()
                    if (body != null) {
                        DeviceData = body.data as ArrayList<DeviceData>
                        setAdapter(DeviceData)
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<DeviceModel>, t: Throwable) {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "통신 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setAdapter(devicelist: List<DeviceData>) {
        val deviceadapter = DeviceListAdapter(requireContext(),devicelist) {device ->
            comItemClicked(device)
        }
        binding.devicerecyclerView.adapter = deviceadapter
        binding.devicerecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.devicerecyclerView.setHasFixedSize(false)
    }

    private fun comItemClicked(device: DeviceData) {
        val nIntent = Intent(requireContext(), PlasmaDetailActivity::class.java)
        if (device != null) {
            nIntent.putExtra("devicename", device.devicename)
            nIntent.putExtra("port", device.port)
        }
//        nIntent.putExtra("SN", bee?.EQPMN_ESNTL_SN)
       startActivity(nIntent)
    }
}