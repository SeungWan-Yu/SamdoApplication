package com.smarthive.samdoapplication.request

class Request(
) {
}

class JoinRequest(
    val USER_ID: String,
    val USER_NAM : String,
    val USER_PASS : String,
    val USER_EMAIL : String,
    val USER_PHONE : String,
    val USER_ADRESS : String,
    val TOKEN : String
){
}

class LoginRequest(
    val USER_ID: String,
    val USER_PASS : String
){
}

class DeviceRequest(
    val PLSM_ID : String
){
}

class RegistDeviceRequest(
    val PLSM_ID : String,
    val PLSM_PORT : String,
    val PLSM_IP : String
){
}

class DeviceModify(
    val PLSM_ID : String,
    val ADDR :Int,
    val DATA : String
){
}

class SensorRequest(
    val SENSOR_ID : String
){
}

class RegistSensorRequest(
    val SENSOR_ID : String,
    val SENSOR_ADDR : String,
    val ADDR_TYPE : String,
    val SENSOR_PORT : String,
    val SENSOR_IP : String,
    val SENSOR_MEMORY : String
){
}
class RegistFarmRequest(
    val FARM_ID : String,
    val USER_ID : String,
    val GPS_LATITUDE : Double,
    val GPS_LONGITUDE : Double,
    val REMARK : String
)

class FarmListRequest(
    val USER_ID : String
)

class FarmRemoveRequest(
    val FARM_ID : String,
    val USER_ID : String
)