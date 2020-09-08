package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("status_code")
    var status: String,

    @SerializedName("auth_token")
    var auth_token: String,

    @SerializedName("user")
    var user: User
)