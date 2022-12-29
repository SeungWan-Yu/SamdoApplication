package com.smarthive.samdoapplication.request

class UserRequest(
) {
}

class SignupRequest(
    val USER_EMAIL: String,
    val USER_PASS : String,
    val USER_NAME : String
){
}

class LoginRequest(
    val USER_EMAIL: String,
    val USER_PASS : String
){
}