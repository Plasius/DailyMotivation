package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("status")
    var status : String
)