package com.example.samdoapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.example.samdoapplication.App
import com.example.samdoapplication.LoadingDialog
import com.example.samdoapplication.R
import com.example.samdoapplication.databinding.ActivityPlasmaDetailBinding
import com.example.samdoapplication.databinding.ActivitySensorDetailBinding
import com.example.samdoapplication.model.Devicerespon
import com.example.samdoapplication.model.Modifyrespon
import com.example.samdoapplication.model.Sensorrespon
import com.example.samdoapplication.request.DeviceModify
import com.example.samdoapplication.request.DeviceRequest
import com.example.samdoapplication.request.SensorRequest
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorDetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySensorDetailBinding
    var sensorname = ""
    var port = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_detail)

        if (intent.hasExtra("sensorname")) {
            sensorname= intent.getStringExtra("sensorname").toString()
            port= intent.getIntExtra("port",0)
            this.supportActionBar?.title = sensorname
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            Toast.makeText(applicationContext, "$port", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "전달된 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }

        getSensorState()

        binding.srefreshLayout.setOnRefreshListener {
            getSensorState()
            binding.srefreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
        }
//
//        binding.btnAuto.setOnClickListener(this)
//        binding.btnSchedule.setOnClickListener(this)
//        binding.btnManual.setOnClickListener(this)
//        binding.btnPlasma.setOnClickListener(this)
//        binding.btnFan.setOnClickListener(this)
//        binding.btnPump.setOnClickListener(this)
//        binding.btnFanauto.setOnClickListener(this)
//        binding.btnFanon.setOnClickListener(this)
    }

//    override fun onClick(view:View){
//            when(view.id){
//                binding.btnAuto.id -> {
//                    App.retrofitService.modify(DeviceModify(devicename,101,"Auto")).enqueue(object : Callback<Modifyrespon>{
//                        override fun onResponse(
//                            call: Call<Modifyrespon>,
//                            response: Response<Modifyrespon>
//                        ) {
//                            val body = response.body()
//                            if (body != null){
//                                if (body.result == "true"){
//                                    getDeviceState2()
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
//                            Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
//                binding.btnSchedule.id -> {
//                    App.retrofitService.modify(DeviceModify(devicename,101,"Timer")).enqueue(object : Callback<Modifyrespon>{
//                        override fun onResponse(
//                            call: Call<Modifyrespon>,
//                            response: Response<Modifyrespon>
//                        ) {
//                            val body = response.body()
//                            if (body != null){
//                                if (body.result == "true"){
//                                    getDeviceState2()
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
//                            Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
//                binding.btnManual.id -> {
//                    App.retrofitService.modify(DeviceModify(devicename,101,"Manual")).enqueue(object : Callback<Modifyrespon>{
//                        override fun onResponse(
//                            call: Call<Modifyrespon>,
//                            response: Response<Modifyrespon>
//                        ) {
//                            val body = response.body()
//                            if (body != null){
//                                if (body.result == "true"){
//                                    getDeviceState2()
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
//                            Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
//                binding.btnFanauto.id -> {
//                    App.retrofitService.modify(DeviceModify(devicename,112,"Auto")).enqueue(object : Callback<Modifyrespon>{
//                        override fun onResponse(
//                            call: Call<Modifyrespon>,
//                            response: Response<Modifyrespon>
//                        ) {
//                            val body = response.body()
//                            if (body != null){
//                                if (body.result == "true"){
//                                    getDeviceState2()
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
//                            Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
//                binding.btnFanon.id -> {
//                    App.retrofitService.modify(DeviceModify(devicename,112,"On")).enqueue(object : Callback<Modifyrespon>{
//                        override fun onResponse(
//                            call: Call<Modifyrespon>,
//                            response: Response<Modifyrespon>
//                        ) {
//                            val body = response.body()
//                            if (body != null){
//                                if (body.result == "true"){
//                                    getDeviceState2()
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
//                            Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
//            }
//    }


    private fun getSensorState() {
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = LoadingDialog(this@SensorDetailActivity)
            dialog.show()
            App.retrofitService.getsensor(SensorRequest(sensorname)).enqueue(object : Callback<Sensorrespon>{
                override fun onResponse(call: Call<Sensorrespon>, response: Response<Sensorrespon>) {
                    val body = response.body()
                    if (body != null){
                        if (body.result == "true"){
                            val data = body.data
                            binding.tvPM.setText("${data.PM25} PPM")
                            binding.tvH2S.setText("${data.H2S} PPM")
                            binding.tvNH3.setText("${data.NH3} PPM")
                            binding.tvCH2O.setText("${data.CH2O} PPM")
                            binding.tvTEMP.setText("${data.TEMP} ℃")
                            binding.tvHUMI.setText("${data.HUMI} %")
                            binding.tvVOCS.setText("${data.VOCS} PPM")
                            binding.tvO3.setText("${String.format("%.3f",data.O3)} PPM")
                        }
                    }
                    dialog.dismiss()
                }
                override fun onFailure(call: Call<Sensorrespon>, t: Throwable) {
                    dialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}