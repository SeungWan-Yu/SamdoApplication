package com.smarthive.samdoapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityPopupSensorBinding
import com.smarthive.samdoapplication.model.Sensorrespon
import com.smarthive.samdoapplication.request.RegistSensorRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorPopupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopupSensorBinding
    lateinit var resultListner : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_popup_sensor)

        resultListner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                fromSub ->
            when(fromSub.resultCode){
                0 -> {
                    val datas = fromSub.data?.getStringExtra("data")
                    if(datas != null){
                        binding.adress.setText(datas)
                    }
                }
            }
        }

        binding.addressFind.setOnClickListener {
            val nIntent = Intent(this, AdressSearchActivity::class.java)
            resultListner.launch(nIntent)
        }
        binding.cancleButton.setOnClickListener {
            setResult(4)
            finish()
        }
        binding.successButton.setOnClickListener {
            var name = binding.sensorName.text
            var port = binding.sensorPort.text
            var ip = binding.sensorIp.text
            var memory = binding.sensorMemory.text
            val address = binding.adress.text

            if (name.toString() == "" || port.toString() == "" || ip.toString() == ""
                || memory.toString() == "" || address.toString() == ""){
                Toast.makeText(this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show()
            }else {
                App.retrofitService.registsensor(
                    RegistSensorRequest(
                        name.toString(),
                        App.prefs.myId.toString(),
                        port.toString(),
                        ip.toString(),
                        memory.toString(),
                        "ROAD",
                        address.toString()
                    )
                ).enqueue(object :
                    Callback<Sensorrespon> {
                    override fun onResponse(
                        call: Call<Sensorrespon>,
                        response: Response<Sensorrespon>
                    ) {
                        val body = response.body()
                        if (body != null) {
                            if (body.result == "true") {
                                setResult(0)
                            } else {
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
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
