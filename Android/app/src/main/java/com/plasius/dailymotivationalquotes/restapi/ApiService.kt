package com.plasius.dailymotivationalquotes.restapi

import com.plasius.dailymotivationalquotes.model.LoginRequest
import com.plasius.dailymotivationalquotes.model.LoginResponse
import com.plasius.dailymotivationalquotes.model.Message
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    @FormUrlEncoded
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET(Constants.MESSAGE_URL)
    fun fetchMessage(@Header("Authorization") token: String): Call<Message>
}