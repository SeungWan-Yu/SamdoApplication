package com.example.samdoapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.example.samdoapplication.databinding.ActivityPlasmaDetailBinding
import com.example.samdoapplication.model.Devicerespon
import com.example.samdoapplication.model.Modifyrespon
import com.example.samdoapplication.request.DeviceModify
import com.example.samdoapplication.request.DeviceRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlasmaDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPlasmaDetailBinding
    var devicename = ""
    var port = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_plasma_detail)

        if (intent.hasExtra("devicename")) {
            devicename= intent.getStringExtra("devicename").toString()
            port= intent.getIntExtra("port",0)
            this.supportActionBar?.title = devicename
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            Toast.makeText(applicationContext, "$port", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "전달된 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }

        getDeviceState()

        binding.btnAuto.setOnClickListener(this)
        binding.btnSchedule.setOnClickListener(this)
        binding.btnManual.setOnClickListener(this)
        binding.btnPlasma.setOnClickListener(this)
        binding.btnFan.setOnClickListener(this)
        binding.btnPump.setOnClickListener(this)
        binding.btnFanauto.setOnClickListener(this)
        binding.btnFanon.setOnClickListener(this)
    }

    override fun onClick(view:View){
        when(view.id){
            binding.btnAuto.id -> {
                App.retrofitService.modify(DeviceModify(devicename,101,"Auto")).enqueue(object : Callback<Modifyrespon>{
                    override fun onResponse(
                        call: Call<Modifyrespon>,
                        response: Response<Modifyrespon>
                    ) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                                getDeviceState()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            binding.btnSchedule.id -> {
                App.retrofitService.modify(DeviceModify(devicename,101,"Timer")).enqueue(object : Callback<Modifyrespon>{
                    override fun onResponse(
                        call: Call<Modifyrespon>,
                        response: Response<Modifyrespon>
                    ) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                                getDeviceState()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            binding.btnManual.id -> {
                App.retrofitService.modify(DeviceModify(devicename,101,"Manual")).enqueue(object : Callback<Modifyrespon>{
                    override fun onResponse(
                        call: Call<Modifyrespon>,
                        response: Response<Modifyrespon>
                    ) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                                getDeviceState()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            binding.btnFanauto.id -> {
                App.retrofitService.modify(DeviceModify(devicename,112,"Auto")).enqueue(object : Callback<Modifyrespon>{
                    override fun onResponse(
                        call: Call<Modifyrespon>,
                        response: Response<Modifyrespon>
                    ) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                                getDeviceState()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            binding.btnFanon.id -> {
                App.retrofitService.modify(DeviceModify(devicename,112,"On")).enqueue(object : Callback<Modifyrespon>{
                    override fun onResponse(
                        call: Call<Modifyrespon>,
                        response: Response<Modifyrespon>
                    ) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                                getDeviceState()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Modifyrespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun getDeviceState() {
        App.retrofitService.getdevice(DeviceRequest(devicename)).enqueue(object : Callback<Devicerespon>{
            override fun onResponse(call: Call<Devicerespon>, response: Response<Devicerespon>) {
                val body = response.body()
                if (body != null){
                    if (body.result == "true"){
                        val data = body.data
                        binding.errorinfo.setText(data.error.toString())
                        when(data.actMode){
                            2 -> {
                                binding.btnAuto.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnSchedule.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnManual.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            3 -> {binding.btnAuto.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnSchedule.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnManual.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            4 -> {binding.btnAuto.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnSchedule.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnManual.setBackgroundColor(Color.parseColor("#3B85AF"))
                            }
                        }
                        when(data.actStat){
                            1 -> {
                                binding.btnPlasma.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnFan.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnPump.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            2 -> {
                                binding.btnPlasma.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnFan.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnPump.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            3 -> {
                                binding.btnPlasma.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnFan.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnPump.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            4 -> {

                                binding.btnPlasma.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnFan.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnPump.setBackgroundColor(Color.parseColor("#3B85AF"))
                            }
                        }
                        binding.tvDate.setText("${data.day[0]}년 ${data.day[1]}월 ${data.day[2]}일")
                        binding.tvTime.setText("${data.time[0]}시 ${data.time[1]}분 ${data.time[2]}초")
                        binding.tvStarttime.setText("${data.tmOn[0]}시 ${data.tmOn[1]}분 ${data.tmOn[2]}초")
                        binding.tvEndtime.setText("${data.tmOff[0]}시 ${data.tmOff[1]}분 ${data.tmOff[2]}초")
                        binding.tvLoopon.setText("${data.rpOn}분")
                        binding.tvLoopoff.setText("${data.rpOff}분")
                        binding.tvPumptime.setText("${data.pump}초")
                        when(data.fan){
                            1 -> {
                                binding.btnFanauto.setBackgroundColor(Color.parseColor("#3B85AF"))
                                binding.btnFanon.setBackgroundColor(Color.parseColor("#3D4047"))
                            }
                            0 -> {
                                binding.btnFanauto.setBackgroundColor(Color.parseColor("#3D4047"))
                                binding.btnFanon.setBackgroundColor(Color.parseColor("#3B85AF"))
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<Devicerespon>, t: Throwable) {
                Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}