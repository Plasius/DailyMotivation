package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

data class Message (
    @SerializedName("id")
    var id: Int,

    @SerializedName("quote")
    var quote: String,

    @SerializedName("imgUrl")
    var imgurl: String
)