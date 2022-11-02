package com.example.samdoapplication.Activity

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.samdoapplication.App
import com.example.samdoapplication.R
import com.example.samdoapplication.databinding.ActivityPopupPlasmaBinding
import com.example.samdoapplication.databinding.ActivityPopupSensorBinding
import com.example.samdoapplication.model.Registrespon
import com.example.samdoapplication.model.Sensorrespon
import com.example.samdoapplication.request.RegistDeviceRequest
import com.example.samdoapplication.request.RegistSensorRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorPopupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopupSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_popup_sensor)

        binding.cancleButton.setOnClickListener {
            finish()
        }
        binding.successButton.setOnClickListener {
            var name = binding.sensorName
            var port = binding.sensorPort
            var ip = binding.sensorIp
            var memory = binding.sensorMemory

            App.retrofitService.registsensor(RegistSensorRequest(name.toString(),"반룡로109","ROAD",port.toString(),ip.toString(),memory.toString())).enqueue(object :
                Callback<Sensorrespon> {
                override fun onResponse(
                    call: Call<Sensorrespon>,
                    response: Response<Sensorrespon>
                ) {
                    val body = response.body()
                    if (body != null){
                        if (body.result == "true"){
                            setResult(0)
                        }else{
                            setResult(1)
                        }
                    }
                    finish()
                }
                override fun onFailure(call: Call<Sensorrespon>, t: Throwable) {
                    setResult(2)
                    finish()
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
