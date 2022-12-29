package com.smarthive.samdoapplication.Activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivitySignUpBinding
import com.smarthive.samdoapplication.model.LoginpModel
import com.smarthive.samdoapplication.model.SignupModel
import com.smarthive.samdoapplication.request.DeviceListRequest
import com.smarthive.samdoapplication.request.SignupRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.error
import kotlin.concurrent.timer

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding

    var idCheck = 0
    private var phonenum = ""
    var timercheck = 0
    var phoneChk = 0
    var minute = 0
    var second = 0
    var certistate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        this.supportActionBar?.title = "회원가입"

        /**
         * 입력된 핸드폰 번호로 인증문자 전송
         * 중복 실행, 무작위 실행을 방지하기 위해 3분에 한번만 가능
         * timercheck == 1일경우는 3분 타이머가 진행중인 것
         * timercheck == 2일경우는 인증이 올바로 이루어진 상태
         * 1,2가 아니면 minute(분)을 3분으로 지정 후 타이머 시작
         * */



//        checkbnt.setOnClickListener {
//            phonenum = suphonenum.text.trim().toString()
//
//            if (phonenum == ""){
//                pwwtext2.isVisible = true
//                pwwtext2.text = getString(R.string.enter_phone)
//           }else{
//                when (timercheck) {
//                    1 -> {
//                        toast(getString(R.string.already_certification))
//                    }
//                    2 -> {
//                        toast(getString(R.string.certification_completion))
//                    }
//                    else -> {
//                        /**
//                         * 인증코드 요청 API통신*/
//                        App.retrofitService.getCertification(CertiRequest("singup",phonenum)).enqueue(object : Callback<CertiResModel>{
//                            override fun onResponse(call: Call<CertiResModel>, response: Response<CertiResModel>) {
//                                val body = response.body()
//                                Log.e("ee",body.toString())
//                                if (body != null) {
//                                    if(body.result == "success"){
//                                        phoneChk = 0
//                                        toast(getString(R.string.success))
//                                        minute = 3
//                                        println("성공후 초 체크")
//                                        println(second)
//                                        timerstart()
//                                        timercheck = 1
//                                    }else{
//                                        if(body.message == getString(R.string.duplicate_phone)){
//                                            toast(getString(R.string.already_registration_phone))
//                                            phoneChk = 1
//                                        }else{
//                                            Log.e("서버에러콘솔",body.message)
//                                            Toast.makeText(applicationContext, getString(R.string.server_error)+body.message, Toast.LENGTH_SHORT).show()
//                                        }
//                                    }
//                                }
//                            }
//
//                            override fun onFailure(call: Call<CertiResModel>, t: Throwable) {
//                                toast(getString(R.string.network))
//                                println("aaa"+t.message)
//                                println(t.toString())
//                            }
//                        })
//                    }
//                }
//            }
//        }
//        recheck.setOnClickListener {
//            val checknum = checknum.text.trim().toString()
//
//            if (checknum == ""){
//                pwwtext3.isVisible = true
//                pwwtext3.text = getString(R.string.enter_certification)
//            }else{
//                //인증 되었을 때
//                if(certistate == 1){
//                    Log.d("aaa","인증 완료")
//                    toast(getString(R.string.certification_completion))
//                }else {
//
//                    App.retrofitService.getCertificationCode(CertiCodeRequest(checknum,phonenum)).enqueue(object : Callback<CertiCodeResModel>{
//                        override fun onResponse(call: Call<CertiCodeResModel>, response: Response<CertiCodeResModel>) {
//                            val body = response.body()
//
//                            if(body?.result == "success"){
////                                second = 100 //인증 완료시
//                                timercheck = 2
//                                certistate = 1
//                            }else{
//                                timercheck = 3
//                                toast(getString(R.string.fail_certification))
//                            }
//                        }
//
//                        override fun onFailure(call: Call<CertiCodeResModel>, t: Throwable) {
//                            toast(getString(R.string.network)+t.message)
//                        }
//
//                    })
//                }
//            }
//        }

        binding.password.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.passwordcheck.text.trim().toString() == binding.password.text.trim().toString()){
                    binding.pwwtext4.isVisible = true
                    binding.pwwtext4.setTextColor(Color.BLUE)
                    binding.pwwtext4.text = "비밀번호가 일치합니다"


                }else{
                    binding.pwwtext4.isVisible = true
                    binding.pwwtext4.text = "비밀번호가 일치하지 않습니다"
                }
            }

        })

        binding.passwordcheck.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.password.text.trim().toString() == binding.passwordcheck.text.trim().toString()){
                    binding.pwwtext4.isVisible = true
                    binding.pwwtext4.setTextColor(Color.BLUE)
                    binding.pwwtext4.text = "비밀번호가 일치합니다"
                }else{
                    binding.pwwtext4.isVisible = true
                    binding.pwwtext4.text = "비밀번호가 일치하지 않습니다"
                }
            }

        })

        binding.idcheck.setOnClickListener {
            val id = binding.sid.text.trim().toString()

            if(id == ""){
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }else{
                App.retrofitService.idCheck(DeviceListRequest(id)).enqueue(object : Callback<LoginpModel>{
                    override fun onResponse(call: Call<LoginpModel>, response: Response<LoginpModel>) {
                        val body = response.body()

                        when (body?.result) {
                            true -> {
                                idCheck = 1
                                Toast.makeText(this@SignUpActivity, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show()
                                binding.sid.isEnabled = false
                            }
                            false -> {
                                Toast.makeText(this@SignUpActivity, "이미 사용중인 아이디 입니다", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this@SignUpActivity, "에러", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginpModel>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "네트워크 에러", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

        binding.sucomplete.setOnClickListener {
            val id = binding.sid.text.trim().toString()
            val idisExist = id.indexOf("@") != -1
            val name = binding.name.text.trim().toString()
//            val phone = binding.suphonenum.text.trim().toString()
            val password = binding.password.text.trim().toString()
            val passwordcheck = binding.passwordcheck.text.trim().toString()
            val token = App.prefs.token?.trim().toString()

            if (password == "" && passwordcheck == ""){
                binding.pwwtext.isVisible = true
                binding.pwwtext.text = "비밀번호 입력"
//                pwwtext4.isVisible = true
//                pwwtext4.text = "비밀번호를 입력해주세요."
            }else if (password != passwordcheck){
                binding.pwwtext.isVisible = true
                binding.pwwtext.text = "비밀번호가 일치하지 않습니다"
            }else if(idCheck == 0){
                Toast.makeText(this, "아이디 중복 검사를 해주세요", Toast.LENGTH_SHORT).show()
//            }else if(certistate == 0) {
//                Toast.makeText(this, "인증을 확인 해주세요", Toast.LENGTH_SHORT).show()
            }else if (idisExist == false){
                Toast.makeText(this, "올바른 이메일 형식을 입력하세요", Toast.LENGTH_SHORT).show()
//            }else if(phone == ""){
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
//            }else if(phone.length != 11){
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }else{
                //비밀번호 암호화 솔팅(salting) + 키 스트레칭(key stretching)
//                val passwordHashed = BCrypt.hashpw(passwordcheck, BCrypt.gensalt(10))
                App.retrofitService.signup(SignupRequest(id,password,name)).enqueue(object : Callback<SignupModel>{
                    override fun onResponse(call: Call<SignupModel>, response: Response<SignupModel>) {
                        val body = response.body()
                        Log.d("aaa",body.toString())
                        when (body?.loginState) {
                            true -> {
                                Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            false -> {
                                Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this@SignUpActivity, "에러", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "네트워크 에러", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }

    }

    //타이머시작
    @SuppressLint("SetTextI18n")
    fun timerstart() {
        timer(period = 1000,initialDelay = 1000){
            if(second == 0){
                if(minute == 0){
                    binding.tvTimer.text = "인증 시간 만료, 다시 시도해주세요"
                    timercheck = 0
                    cancel()
                }else{
                    binding.tvTimer.text = "0$minute:0$second"
                    second = 59
                    minute -= 1
                }
            }else{
                if(second.toString().length == 1){
                    binding.tvTimer.text = "0$minute:0$second"
                    second -= 1
                }else {
                    binding.tvTimer.text = "0$minute:$second"
                    second -= 1
                }
            }

            println("타이머체크>>$timercheck")
            if(timercheck==2){
                binding.tvTimer.setTextColor(Color.BLUE)
                binding.tvTimer.text = "인증 성공"
                cancel()
            }else if(timercheck==3){
                binding.tvTimer.text = "인증 실패"
                second = 0
                cancel()
            }
//                else if(second == 100){
//                tv_timer.setText("인증 성공")
//                timercheck = 2
//                certistate = 1
//                cancel()
//                }
        }
    }
}