package com.smarthive.samdoapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.Fragment.addr
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivitySensorDetailBinding
import com.smarthive.samdoapplication.model.SensorControlrespon
import com.smarthive.samdoapplication.model.Sensorrespon
import com.smarthive.samdoapplication.request.ControlSensor
import com.smarthive.samdoapplication.request.SensorRequest
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorDetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySensorDetailBinding
    var sensorname = ""
    var port = ""
    var idx = 0
    var addr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_detail)

        if (intent.hasExtra("sensorname")) {
            sensorname= intent.getStringExtra("sensorname").toString()
            port= intent.getStringExtra("port").toString()
            idx= intent.getIntExtra("idx",0)
            addr= intent.getStringExtra("addr").toString()
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

        binding.btnH2s.setOnClickListener {
            val h2s = binding.ctlH2s.text.toString().toInt()

            App.retrofitService.controlesensor(ControlSensor(idx.toString(),"H2S",h2s)).enqueue(object : Callback<SensorControlrespon>{
                override fun onResponse(
                    call: Call<SensorControlrespon>,
                    response: Response<SensorControlrespon>
                ) {
                    val body = response.body()
                    if (body != null){
                        if (body.result){
                            Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<SensorControlrespon>, t: Throwable) {
                    Log.e("asd",t.message.toString())
                    Toast.makeText(applicationContext, "통신 오류", Toast.LENGTH_SHORT).show()
                }
            })

        }
        binding.btnNh3.setOnClickListener {
            val nh3 = binding.ctlNh3.text.toString().toInt()

            App.retrofitService.controlesensor(ControlSensor(idx.toString(),"NH3",nh3)).enqueue(object : Callback<SensorControlrespon>{
                override fun onResponse(
                    call: Call<SensorControlrespon>,
                    response: Response<SensorControlrespon>
                ) {
                    val body = response.body()
                    if (body != null){
                        if (body.result){
                            Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<SensorControlrespon>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신 오류", Toast.LENGTH_SHORT).show()
                }
            })
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
            App.retrofitService.getsensor(SensorRequest(idx.toString())).enqueue(object : Callback<Sensorrespon>{
                override fun onResponse(call: Call<Sensorrespon>, response: Response<Sensorrespon>) {
                    val body = response.body()
                    if (body != null){
                        if (body.result){
                            val data = body.data
                            binding.tvPM.text = "${data.PM25} PPM"
                            binding.tvH2S.text = "${data.H2S} PPM"
                            binding.tvNH3.text = "${data.NH3} PPM"
                            binding.tvCH2O.text = "${data.CH2O} PPM"
                            binding.tvTEMP.text = "${data.TEMP} ℃"
                            binding.tvHUMI.text = "${data.HUMI} %"
                            binding.tvVOCS.text = "${String.format("%.3f",data.VOCS)} PPM"
                            binding.tvO3.text = "${String.format("%.3f",data.O3)} PPM"
                            binding.ctlH2s.setText("${data.CTL_H2S}")
                            binding.ctlNh3.setText("${data.CTL_NH3}")
                            val data2 = body.data.WEATHER
                            binding.tvAddr.text = data2.ADDR
                            binding.tvTime.text = data2.TMSP
                            var pty = ""
                            when(data2.PTY){
                                0 -> pty = "맑음"
                                1 -> pty = "비"
                                2 -> pty = "비/눈"
                                3 -> pty = "눈"
                                4 -> pty = ""
                                5 -> pty = "빗방울"
                                6 -> pty = "빗방울/눈날림"
                                7 -> pty = "눈날림"
                            }
                            binding.tvPty.text = pty
                            binding.tvRn1.text = "${data2.RN1} mm/h"
                            binding.tvT1h.text = "${data2.T1H} ℃"
                            binding.tvReh.text = "${data2.REH} %"
                            binding.tvVec.text = "${data2.VEC} º"
                            binding.tvWsd.text = "${data2.WSD} m/s"

                        }
                    }
                    dialog.dismiss()
                }
                override fun onFailure(call: Call<Sensorrespon>, t: Throwable) {
                    dialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    Log.e("asdasd",t.message.toString())
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