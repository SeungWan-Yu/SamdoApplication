package com.example.samdoapplication.model

import com.google.gson.annotations.SerializedName

class DeviceModel(
    @SerializedName("devicename") val devicename : String,
    @SerializedName("port") val port : String,
    @SerializedName("protocol") val protocol : String
)