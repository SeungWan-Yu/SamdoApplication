package com.smarthive.samdoapplication.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.InputDevice.getDevice
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.smarthive.samdoapplication.adapter.DeviceListAdapter
import com.smarthive.samdoapplication.adapter.SensorListAdapter
import com.smarthive.samdoapplication.databinding.FragmentSensorBinding
import com.smarthive.samdoapplication.model.Devicedelete
import com.smarthive.samdoapplication.model.SensorData
import com.smarthive.samdoapplication.model.SensorModel
import com.smarthive.samdoapplication.request.DeviceListRequest
import com.smarthive.samdoapplication.request.DeviceRequest
import com.smarthive.samdoapplication.request.SensorRequest
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
                3 -> { //취소
                    Toast.makeText(requireContext(), "등록 취소", Toast.LENGTH_SHORT).show()
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
            App.retrofitService.getsensorlist(DeviceListRequest(App.prefs.myId.toString())).enqueue(object : Callback<SensorModel> {
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
        sensordapter.itemClick = object : SensorListAdapter.ItemClick{
            override fun onLongClick(view: View, position: Int) {
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(mDialogView)
                    .setTitle("삭제")

                mBuilder.setCancelable(false)
                val mAlertDialog = mBuilder.show()
                val okButton = mDialogView.findViewById<Button>(R.id.successButton)
                okButton.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        App.retrofitService.deletesensor(SensorRequest(sensorlist[position].idx.toString())).enqueue(object : Callback<Devicedelete> {
                            override fun onResponse(call: Call<Devicedelete>, response: Response<Devicedelete>) {
                                val body = response.body()

                                if (body?.result == true && body.data == 1){
                                    Toast.makeText(context, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                                    getSensor()
                                }else{
                                    Toast.makeText(context, "삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<Devicedelete>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                            }
                        })
                        mAlertDialog.dismiss()
                    }
                }
                val noButton = mDialogView.findViewById<Button>(R.id.cancleButton)
                noButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
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
            nIntent.putExtra("idx", device.idx)
            nIntent.putExtra("addr", device.addr)
        }
//        nIntent.putExtra("SN", bee?.EQPMN_ESNTL_SN)
        startActivity(nIntent)
    }


}