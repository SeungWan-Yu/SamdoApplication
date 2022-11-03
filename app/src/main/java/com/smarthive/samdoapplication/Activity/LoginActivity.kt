package com.smarthive.samdoapplication.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityLoginBinding

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

        if (App.prefs.myId != "") {
            Toast.makeText(this, App.prefs.myId+" 님 로그인", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {

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
//                    CoroutineScope(Main).launch {
                        dialog.show()
//                        App.retrofitService.Login(LoginRequest(inputLogin,inputPassword)).enqueue(object : Callback<LoginModel>{
//                            override fun onResponse(
//                                call: Call<LoginModel>,
//                                response: Response<LoginModel>
//                            ) {
                                dialog.dismiss()
//                                val body = response.body()
//
//                                if(body?.LSIND_REGIST_NO == "fail"){
//                                    toast(getString(R.string.user_nomatching))
//                                }else{
                                    App.prefs.myId = inputLogin
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                    intent.putExtra("data",body)
                                    startActivity(intent)
//                                    overridePendingTransition(R.anim.r_slicd_in,R.anim.r_slid_out)
                                    finish()
//                                }
//
//                            }
//
//                            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
//                                dialog.dismiss()
//                                toast(getString(R.string.network))
//                            }
//
//                        })
//                    }
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

}