package com.smarthive.samdoapplication.model

import com.google.gson.annotations.SerializedName

class DeviceModel(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : List<DeviceData>
)

class DeviceData(
    @SerializedName("PLSM_ID") val devicename : String,
    @SerializedName("USER_EMAIL") val email : String,
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

class Devicedelete(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : Int
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
    @SerializedName("IDX") val idx : Int,
    @SerializedName("SENSOR_ID") val sensorname : String,
    @SerializedName("USER_EMAIL") val  email: String,
    @SerializedName("ADDR") val addr : String,
    @SerializedName("GPS_LATITUDE") val latitude : String,
    @SerializedName("GPS_LONGITUDE") val longitude : String,
    @SerializedName("SENSOR_PORT") val port : String,
    @SerializedName("SENSOR_IP") val ip : String,
    @SerializedName("SENSOR_MEMORY") val memory : Int,
    @SerializedName("PRTC_ID") val prtc : String,
    @SerializedName("CTL_S2H") val s2h : Float,
    @SerializedName("CTL_NH3") val nh3 : Float,
    @SerializedName("CTL_PLSM") val plsm : Float,
)

class SensorControlrespon(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : Boolean
)

class Sensorrespon(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : Sensorinfo
)

class Sensorinfo(
    @SerializedName("PM25") val PM25 : Int,
    @SerializedName("H2S") val H2S : Int,
    @SerializedName("NH3") val NH3 : Int,
    @SerializedName("CH2O") val CH2O :Int,
    @SerializedName("TEMP") val TEMP : Float,
    @SerializedName("HUMI") val HUMI : Float,
    @SerializedName("VOCS") val VOCS : Float,
    @SerializedName("O3") val O3 : Float,
    @SerializedName("CTL_S2H") val CTL_H2S : Int,
    @SerializedName("CTL_NH3") val CTL_NH3 : Int,
    @SerializedName("WEATHER") val WEATHER : WeatherInfo
)

class WeatherInfo (
    @SerializedName("IDX") val IDX : Int,
    @SerializedName("ADDR") val ADDR :String,
@SerializedName("X") val X : Int,
@SerializedName("Y") val Y : Int,
@SerializedName("PTY") val PTY : Int,
@SerializedName("REH") val REH : Int,
@SerializedName("RN1") val RN1 : Int,
@SerializedName("T1H") val T1H : Double,
@SerializedName("UUU") val UUU : Double,
@SerializedName("VEC") val VEC : Int,
@SerializedName("VVV") val VVV : Double,
@SerializedName("WSD") val WSD : Double,
@SerializedName("TMSP") val TMSP : String
)

class MapPointModel(
    @SerializedName("result") val result : Boolean,
    @SerializedName("data") val data : List<MapPointData>
)

class MapPointData(
    @SerializedName("SENSOR_IDX") val SENSOR_IDX : Int,
    @SerializedName("MESURE_DT") val MESURE_DT : String,
    @SerializedName("ADDR") val ADDR : String,
    @SerializedName("GPS_LATITUDE") val GPS_LATITUDE : String,
    @SerializedName("GPS_LONGITUDE") val GPS_LONGITUDE : String,
    @SerializedName("ODOR") val ODOR : Int
)