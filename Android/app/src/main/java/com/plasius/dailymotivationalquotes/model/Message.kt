package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

//TODO

data class Message (
    @SerializedName("id")
    var id: Int,

    @SerializedName("quote")
    var quote: String,

    @SerializedName("img_url")
    var imgUrl: String
)
