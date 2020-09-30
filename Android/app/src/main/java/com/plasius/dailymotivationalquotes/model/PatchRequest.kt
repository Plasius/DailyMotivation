package com.plasius.dailymotivationalquotes.model

import com.google.gson.annotations.SerializedName

class PatchRequest {
    @SerializedName("email")
    var email: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("password")
    var password: String? = null
}