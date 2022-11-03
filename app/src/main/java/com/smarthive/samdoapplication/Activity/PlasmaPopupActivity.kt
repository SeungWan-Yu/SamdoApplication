package com.smarthive.samdoapplication.Activity

import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityPopupPlasmaBinding
import com.smarthive.samdoapplication.model.Registrespon
import com.smarthive.samdoapplication.request.RegistDeviceRequest
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
            setResult(4)
            finish()
        }
        binding.successButton.setOnClickListener {
            val name = binding.plasmaName.text
            val port = binding.plasmaPort.text
            val ip = binding.plasmaIp.text

            if (name.toString() == "" || port.toString() == "" || ip.toString() == ""){
                Toast.makeText(this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show()
            }else {
                App.retrofitService.registdevice(
                    RegistDeviceRequest(
                        name.toString(),
                        port.toString(),
                        ip.toString()
                    )
                ).enqueue(object : Callback<Registrespon> {
                    override fun onResponse(
                        call: Call<Registrespon>,
                        response: Response<Registrespon>
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

                    override fun onFailure(call: Call<Registrespon>, t: Throwable) {
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
