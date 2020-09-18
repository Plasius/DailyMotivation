package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class ValidateResponse (
    @SerializedName("status")
    val status : String,

    @SerializedName("user")
    var user: User
)