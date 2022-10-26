package com.example.samdoapplication.model

import com.google.gson.annotations.SerializedName

class DeviceModel(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : List<DeviceData>
)

class DeviceData(
    @SerializedName("PLSM_ID") val devicename : String,
    @SerializedName("PLSM_PORT") val port : Int,
    @SerializedName("PLSM_IP") val ip : String,
    @SerializedName("PRTC_ID") val id : String
)

class Devicerespon(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : Deviceinfo
)

class Modifyrespon(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : Result
)

class Result(
    @SerializedName("success") val success : String
)


class Deviceinfo(
    @SerializedName("actMode") val actMode : Int,
    @SerializedName("actStat") val actStat : Int,
    @SerializedName("error") val error : Int,
    @SerializedName("time") val time : List<String>,
    @SerializedName("day") val day : List<String>,
    @SerializedName("tmOn") val tmOn : List<String>,
    @SerializedName("tmOff") val tmOff : List<String>,
    @SerializedName("rpOn") val rpOn : Int,
    @SerializedName("rpOff") val rpOff : Int,
    @SerializedName("pump") val pump : Int,
    @SerializedName("fan") val fan : Int
)