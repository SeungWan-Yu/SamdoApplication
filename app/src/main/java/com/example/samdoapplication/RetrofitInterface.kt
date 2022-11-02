package com.example.samdoapplication

import com.example.samdoapplication.model.*
import com.example.samdoapplication.request.*
import retrofit2.Call
import retrofit2.http.*
import java.net.CacheRequest


interface RetrofitInterface {

    @POST("/api/plasma/list")
    fun getdevicelist():Call<DeviceModel>

    @POST("/api/plasma/regist")
    fun registdevice(
        @Body request: RegistDeviceRequest
    ):Call<Registrespon>

    @POST("/api/plasma/read")
    fun getdevice(
        @Body request: DeviceRequest
    ): Call<Devicerespon>

    @POST("/api/plasma/modify")
    fun modify(
        @Body request: DeviceModify
    ): Call<Modifyrespon>

    @POST("/api/sensor/list")
    fun getsensorlist():Call<SensorModel>

    @POST("/api/sensor/read")
    fun getsensor(
        @Body request: SensorRequest
    ): Call<Sensorrespon>

    @POST("/api/sensor/regist")
    fun registsensor(
        @Body request: RegistSensorRequest
    ):Call<Sensorrespon>

//    @POST("user/login")
//    fun login(
//        @Body request: LoginRequest
//    ): Call<LoginModel>


}