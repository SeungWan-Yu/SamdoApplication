package com.example.samdoapplication

import com.example.samdoapplication.model.DeviceModel
import com.example.samdoapplication.model.Devicerespon
import com.example.samdoapplication.model.Modifyrespon
import com.example.samdoapplication.request.DeviceModify
import com.example.samdoapplication.request.DeviceRequest
import retrofit2.Call
import retrofit2.http.*
import java.net.CacheRequest


interface RetrofitInterface {

    @POST("/api/plasma/list")
    fun getdevicelist():Call<DeviceModel>

    @POST("/api/plasma/read")
    fun getdevice(
        @Body request: DeviceRequest
    ): Call<Devicerespon>

    @POST("/api/plasma/modify")
    fun modify(
        @Body request: DeviceModify
    ): Call<Modifyrespon>

//    @POST("user/login")
//    fun login(
//        @Body request: LoginRequest
//    ): Call<LoginModel>


}