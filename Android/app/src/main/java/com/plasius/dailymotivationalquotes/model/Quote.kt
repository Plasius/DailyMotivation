package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class Quote (
    @SerializedName("message_id")
    var id : Int,

    @SerializedName("quote")
    var text : String,

)