package com.smarthive.samdoapplication.model

import com.google.gson.annotations.SerializedName

class UserModel(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : List<DeviceData>
)

class SignupModel(
    @SerializedName("loginState") val loginState : Boolean
)

class LoginpModel(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : LogindataModel
)

class LogindataModel(
    @SerializedName("EMAIL") val EMAIL : String,
    @SerializedName("NAME") val NAME : String
)
