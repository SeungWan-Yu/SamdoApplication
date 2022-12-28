package com.smarthive.samdoapplication.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityLoginBinding
import com.smarthive.samdoapplication.model.LoginpModel
import com.smarthive.samdoapplication.request.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//
//        configuration.locale = Locale.US
//
//        resources.updateConfiguration(configuration,resources.displayMetrics)

        // 해시키
        getHashKey()

        if (App.prefs.myId != "") {
            Toast.makeText(this, App.prefs.myId+" 님 로그인", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {

            binding.signup.setOnClickListener {
                val intent = Intent(this,SignUpActivity::class.java)
                startActivity(intent)
            }

            binding.pwview.setOnClickListener {
                binding.etpw.inputType
            }

            binding.login.setOnClickListener {
                val inputLogin = binding.edit.text.trim().toString()
                val inputPassword = binding.etpw.text.trim().toString()

                if (inputLogin == "" || inputPassword == "") {
                    Toast.makeText(this, "유저 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    val dialog = LoadingDialog(this)
                    CoroutineScope(Main).launch {
                        dialog.show()
                        App.retrofitService.login(LoginRequest(inputLogin,inputPassword)).enqueue(object : Callback<LoginpModel>{
                            override fun onResponse(
                                call: Call<LoginpModel>,
                                response: Response<LoginpModel>
                            ) {
                                dialog.dismiss()
                                val body = response.body()

                                if(body?.result == false){
                                    Toast.makeText(this@LoginActivity, "일치하는 유저 정보가 없습니다", Toast.LENGTH_SHORT).show()
                                }else if(body?.result == true){
                                    App.prefs.myId = inputLogin
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("data",body.data.EMAIL)
                                    startActivity(intent)
//                                    overridePendingTransition(R.anim.r_slicd_in,R.anim.r_slid_out)
                                    finish()
                                }else{
                                    Toast.makeText(this@LoginActivity, "에러", Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginpModel>, t: Throwable) {
                                dialog.dismiss()
                                Toast.makeText(this@LoginActivity, "네트워크 에러 입니다", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }
            }
//            signup.setOnClickListener {
//                startActivity<SignUpActivity>()
//            }
//            findpw.setOnClickListener {
//                startActivity<FindPWActivity>()
//            }
        }
    }
    private fun getHashKey(){
        var packageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for (signature: Signature in packageInfo.signatures){
            try{
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                binding.textView.text = key
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))

            } catch (e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = $signature", e)
            }
        }
    }
}