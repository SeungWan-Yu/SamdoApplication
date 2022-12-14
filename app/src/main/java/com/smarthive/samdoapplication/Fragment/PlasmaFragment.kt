package com.smarthive.samdoapplication.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.smarthive.samdoapplication.model.Devicedelete
import com.smarthive.samdoapplication.request.DeviceListRequest
import com.smarthive.samdoapplication.request.DeviceRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.error

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
                3 -> { //취소
                    Toast.makeText(requireContext(), "등록 취소", Toast.LENGTH_SHORT).show()
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
            App.retrofitService.getdevicelist(DeviceListRequest(App.prefs.myId.toString())).enqueue(object : Callback<DeviceModel> {
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
                    Log.e("asdasd",t.message.toString())
                }
            })
        }
    }

    private fun setAdapter(devicelist: List<DeviceData>) {
        val deviceadapter = DeviceListAdapter(requireContext(),devicelist) {device ->
            comItemClicked(device)
        }
        deviceadapter.itemClick = object : DeviceListAdapter.ItemClick{
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
                        App.retrofitService.deleteplasma(DeviceRequest(devicelist[position].devicename)).enqueue(object : Callback<Devicedelete> {
                            override fun onResponse(call: Call<Devicedelete>, response: Response<Devicedelete>) {
                                val body = response.body()

                                if (body?.result == true && body.data == 1){
                                    Toast.makeText(context, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                                    getDevice()
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