package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class StatusResponse (
    @SerializedName("status")
    var status : String
)