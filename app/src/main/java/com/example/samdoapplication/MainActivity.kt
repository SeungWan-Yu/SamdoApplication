package com.example.samdoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samdoapplication.adapter.DeviceListAdapter
import com.example.samdoapplication.databinding.ActivityMainBinding
import com.example.samdoapplication.model.DeviceData
import com.example.samdoapplication.model.DeviceModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var DeviceData = ArrayList<DeviceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        getDevice()

        this.supportActionBar?.title = "삼도환경"
    }



    private fun getDevice(){

        App.retrofitService.getdevicelist().enqueue(object : Callback<DeviceModel>{
            override fun onResponse(call: Call<DeviceModel>, response: Response<DeviceModel>) {
                val body = response.body()
                if (body != null) {
                    DeviceData = body.data as ArrayList<DeviceData>
                    setAdapter(DeviceData)
                }
            }
            override fun onFailure(call: Call<DeviceModel>, t: Throwable) {

            }
        })
    }

    private fun setAdapter(devicelist: List<DeviceData>) {
        val deviceadapter = DeviceListAdapter(this,devicelist) {device ->
            comItemClicked(device)
        }
        binding.devicerecyclerView.adapter = deviceadapter
        binding.devicerecyclerView.layoutManager = LinearLayoutManager(this)
        binding.devicerecyclerView.setHasFixedSize(false)
    }

    private fun comItemClicked(device: DeviceData) {
        val nIntent = Intent(this, PlasmaDetailActivity::class.java)
        if (device != null) {
            nIntent.putExtra("devicename", device.devicename)
            nIntent.putExtra("port", device.port)
        }
//        nIntent.putExtra("SN", bee?.EQPMN_ESNTL_SN)
       startActivity(nIntent)
    }
}