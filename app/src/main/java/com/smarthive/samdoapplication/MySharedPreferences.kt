package com.smarthive.samdoapplication

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context)  {
    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MY_ID = "myEditText"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    var mytoken = "token"
    var mapvalue = 0

    var myId: String?
        get() = prefs.getString(PREF_KEY_MY_ID, "")
        set(value) = prefs.edit().putString(PREF_KEY_MY_ID, value).apply()

    var token: String?
        get() = prefs.getString(mytoken,"")
        set(value) = prefs.edit().putString(mytoken,value).apply()

    var permissionCheck : Int?
        get()  = prefs.getInt("permission", 0)
        set(value) =  prefs.edit().putInt("permission", value!!).apply()

    var permissionDialogCheck : Int?
        get()  = prefs.getInt("permission", 0)
        set(value) =  prefs.edit().putInt("permission", value!!).apply()

    var mapType : Int?
        get()  = prefs.getInt("maptype", 0)
        set(value) =  prefs.edit().putInt("maptype", value!!).apply()


}
