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
import com.example.samdoapplication.model.Registrespon
import com.example.samdoapplication.request.RegistDeviceRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlasmaPopupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopupPlasmaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_popup_plasma)

        binding.cancleButton.setOnClickListener {
            finish()
        }
        binding.successButton.setOnClickListener {
            val name = binding.plasmaName.text
            val port = binding.plasmaPort.text
            val ip = binding.plasmaIp.text

            App.retrofitService.registdevice(RegistDeviceRequest(name.toString(),port.toString(),ip.toString())).enqueue(object : Callback<Registrespon>{
                override fun onResponse(
                    call: Call<Registrespon>,
                    response: Response<Registrespon>
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
                override fun onFailure(call: Call<Registrespon>, t: Throwable) {
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
