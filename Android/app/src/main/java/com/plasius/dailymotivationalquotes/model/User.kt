package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class User (

        @SerializedName("email")
        var email: String,

        @SerializedName("password")
        var password: String,

        @SerializedName("username")
        var username: String,

)