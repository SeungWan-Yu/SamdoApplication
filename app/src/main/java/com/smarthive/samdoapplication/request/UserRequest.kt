package com.smarthive.samdoapplication.request

class UserRequest(
) {
}

class SignupRequest(
    val EMAIL: String,
    val PASS : String,
    val NAME : String
){
}

class LoginRequest(
    val USER_EMAIL: String,
    val USER_PASS : String
){
}