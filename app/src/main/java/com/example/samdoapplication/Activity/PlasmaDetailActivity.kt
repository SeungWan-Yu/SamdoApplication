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
import com.example.samdoapplication.App
import com.example.samdoapplication.LoadingDialog
import com.example.samdoapplication.R
import com.example.samdoapplication.databinding.ActivityPlasmaDetailBinding
import com.example.samdoapplication.model.Devicerespon
import com.example.samdoapplication.model.Modifyrespon
import com.example.samdoapplication.request.DeviceModify
import com.example.samdoapplication.request.DeviceRequest
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlasmaDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPlasmaDetailBinding
    var devicename = ""
    var port = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plasma_detail)

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

        binding.prefreshLayout.setOnRefreshListener {
            getDeviceState()
            binding.prefreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
        }

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
                                getDeviceState2()
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
                                getDeviceState2()
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
                                getDeviceState2()
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
                                getDeviceState2()
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
                                getDeviceState2()
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


    private fun getDeviceState2() {
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = LoadingDialog(this@PlasmaDetailActivity)
            dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                App.retrofitService.getdevice(DeviceRequest(devicename)).enqueue(object : Callback<Devicerespon>{
                    override fun onResponse(call: Call<Devicerespon>, response: Response<Devicerespon>) {
                        val body = response.body()
                        if (body != null){
                            if (body.result == "true"){
                                val data = body.data
                                binding.errorinfo.setText(data.error.toString())
                                when(data.actMode){
                                    2 -> {
                                        btnon(binding.btnAuto)
                                        btnoff(binding.btnSchedule)
                                        btnoff(binding.btnManual)
                                    }
                                    3 -> {
                                        btnoff(binding.btnAuto)
                                        btnon(binding.btnSchedule)
                                        btnoff(binding.btnManual)
                                    }
                                    4 -> {
                                        btnoff(binding.btnAuto)
                                        btnoff(binding.btnSchedule)
                                        btnon(binding.btnManual)
                                    }
                                }
                                when(data.actStat){
                                    1 -> {
                                        btnon(binding.btnPlasma)
                                        btnoff(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    2 -> {
                                        btnoff(binding.btnPlasma)
                                        btnon(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    3 -> {
                                        btnon(binding.btnPlasma)
                                        btnon(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    4 -> {
                                        btnoff(binding.btnPlasma)
                                        btnoff(binding.btnFan)
                                        btnon(binding.btnPump)
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
                                        btnon(binding.btnFanauto)
                                        btnoff(binding.btnFanon)
                                    }
                                    0 -> {
                                        btnoff(binding.btnFanauto)
                                        btnon(binding.btnFanon)
                                    }
                                }
                            }
                        }
                        dialog.dismiss()
                        Toast.makeText(applicationContext, "변경 성공", Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<Devicerespon>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }

                })
            },4000)

        }
    }

    private fun getDeviceState() {
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = LoadingDialog(this@PlasmaDetailActivity)
            dialog.show()
            App.retrofitService.getdevice(DeviceRequest(devicename)).enqueue(object : Callback<Devicerespon>{
                override fun onResponse(call: Call<Devicerespon>, response: Response<Devicerespon>) {
                    val body = response.body()
                    if (body != null){
                        if (body.result == "true"){
                            if (body.data != null) {
                                val data = body.data
                                binding.errorinfo.setText(data.error.toString())
                                when (data.actMode) {
                                    2 -> {
                                        btnon(binding.btnAuto)
                                        btnoff(binding.btnSchedule)
                                        btnoff(binding.btnManual)
                                    }
                                    3 -> {
                                        btnoff(binding.btnAuto)
                                        btnon(binding.btnSchedule)
                                        btnoff(binding.btnManual)
                                    }
                                    4 -> {
                                        btnoff(binding.btnAuto)
                                        btnoff(binding.btnSchedule)
                                        btnon(binding.btnManual)
                                    }
                                }
                                when (data.actStat) {
                                    1 -> {
                                        btnon(binding.btnPlasma)
                                        btnoff(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    2 -> {
                                        btnoff(binding.btnPlasma)
                                        btnon(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    3 -> {
                                        btnon(binding.btnPlasma)
                                        btnon(binding.btnFan)
                                        btnoff(binding.btnPump)
                                    }
                                    4 -> {
                                        btnoff(binding.btnPlasma)
                                        btnoff(binding.btnFan)
                                        btnon(binding.btnPump)
                                    }
                                }
                                binding.tvDate.setText("${data.day[0]}년 ${data.day[1]}월 ${data.day[2]}일")
                                binding.tvTime.setText("${data.time[0]}시 ${data.time[1]}분 ${data.time[2]}초")
                                binding.tvStarttime.setText("${data.tmOn[0]}시 ${data.tmOn[1]}분 ${data.tmOn[2]}초")
                                binding.tvEndtime.setText("${data.tmOff[0]}시 ${data.tmOff[1]}분 ${data.tmOff[2]}초")
                                binding.tvLoopon.setText("${data.rpOn}분")
                                binding.tvLoopoff.setText("${data.rpOff}분")
                                binding.tvPumptime.setText("${data.pump}초")
                                when (data.fan) {
                                    1 -> {
                                        btnon(binding.btnFanauto)
                                        btnoff(binding.btnFanon)
                                    }
                                    0 -> {
                                        btnoff(binding.btnFanauto)
                                        btnon(binding.btnFanon)
                                    }
                                }
                            }
                            else{
                                Toast.makeText(this@PlasmaDetailActivity, "데이터가 없습니다", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                    dialog.dismiss()
                }
                override fun onFailure(call: Call<Devicerespon>, t: Throwable) {
                    dialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun btnon(btnAuto: Button) {
        btnAuto.setBackgroundResource(R.drawable.btn2)
        btnAuto.isEnabled = false
    }
    private fun btnoff(btnSchedule: Button) {
        btnSchedule.setBackgroundResource(R.drawable.btn1)
        btnSchedule.isEnabled = true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}