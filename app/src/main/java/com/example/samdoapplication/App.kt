package com.example.samdoapplication

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
//    가장먼저 실행되어 초기화 시켜줘야 함
//    Manifest application에 android:name=".App" 추가

    companion object {
        lateinit var prefs : MySharedPreferences
        val url = "http://env.kro.kr"//서버 주소
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)

    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}