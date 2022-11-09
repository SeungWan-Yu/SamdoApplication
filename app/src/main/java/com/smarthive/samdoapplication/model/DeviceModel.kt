package com.smarthive.samdoapplication.model

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
class Registrespon(
    @SerializedName("result") val result: String,
    @SerializedName("data") val data: DeviceData
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

class SensorModel(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : List<SensorData>
)

class SensorData(
    @SerializedName("SENSOR_ID") val sensorname : String,
    @SerializedName("GPS_LATITUDE") val latitude : String,
    @SerializedName("GPS_LONGITUDE") val longitude : String,
    @SerializedName("SENSOR_PORT") val port : String,
    @SerializedName("SENSOR_IP") val ip : String,
    @SerializedName("SENSOR_MEMORY") val memory : Int,
    @SerializedName("PRTC_ID") val prtc : String
)

class Sensorrespon(
    @SerializedName("result") val result : String,
    @SerializedName("data") val data : Sensorinfo
)


class Sensorinfo(
    @SerializedName("PM25") val PM25 : Int,
    @SerializedName("H2S") val H2S : Int,
    @SerializedName("NH3") val NH3 : Int,
    @SerializedName("CH2O") val CH2O :Int,
    @SerializedName("TEMP") val TEMP : Float,
    @SerializedName("HUMI") val HUMI : Float,
    @SerializedName("VOCS") val VOCS : Int,
    @SerializedName("O3") val O3 : Double,
    @SerializedName("CTL_S2H") val CTL_H2S : Int,
    @SerializedName("CTL_NH3") val CTL_NH3 : Int
)