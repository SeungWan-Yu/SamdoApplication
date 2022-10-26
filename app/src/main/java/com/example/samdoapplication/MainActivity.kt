package com.example.samdoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samdoapplication.adapter.DeviceListAdapter
import com.example.samdoapplication.databinding.ActivityMainBinding
import com.example.samdoapplication.model.DeviceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var DeviceData = ArrayList<DeviceModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        getDevice()
    }

    private fun getDevice(){
        DeviceData.add(DeviceModel("plasma1","8001","0000"))
        DeviceData.add(DeviceModel("plasma2","8002","0000"))

        setAdapter(DeviceData)
    }

    private fun setAdapter(devicelist: List<DeviceModel>) {
        val deviceadapter = DeviceListAdapter(this,devicelist) {device ->
            comItemClicked(device)
        }
        binding.devicerecyclerView.adapter = deviceadapter
        binding.devicerecyclerView.layoutManager = LinearLayoutManager(this)
        binding.devicerecyclerView.setHasFixedSize(false)
    }

    private fun comItemClicked(device: DeviceModel) {

    }
}