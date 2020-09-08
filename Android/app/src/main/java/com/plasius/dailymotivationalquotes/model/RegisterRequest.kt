package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String

)