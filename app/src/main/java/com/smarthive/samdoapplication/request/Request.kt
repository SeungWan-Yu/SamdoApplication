package com.smarthive.samdoapplication.request

class Request(
) {
}

class DeviceListRequest(
    val USER_EMAIL : String
)

class DeviceRequest(
    val PLSM_ID : String
){
}

class RegistDeviceRequest(
    val PLSM_ID : String,
    val USER_EMAIL : String,
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
    val SENSOR_IDX : String
){
}

class RegistSensorRequest(
    val SENSOR_ID : String,
    val USER_EMAIL : String,
    val SENSOR_PORT : String,
    val SENSOR_IP : String,
    val SENSOR_MEMORY : String,
    val ADDR_TYPE : String,
    val SENSOR_ADDR : String,
){
}
class RegistFarmRequest(
    val FARM_ID : String,
    val USER_ID : String,
    val GPS_LATITUDE : Double,
    val GPS_LONGITUDE : Double,
    val REMARK : String
)

class ControlSensor(
    val SENSOR_IDX : String,
    val TYPE : String,
    val DATA : Int
)

class FarmRemoveRequest(
    val FARM_ID : String,
    val USER_ID : String
)