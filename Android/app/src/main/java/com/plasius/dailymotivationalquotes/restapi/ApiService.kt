package com.plasius.dailymotivationalquotes.restapi

import com.plasius.dailymotivationalquotes.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constants.REGISTER_URL)
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    //@GET(Constants.QUOTES_URL)
    //fun fetchMessage(@Header("Authorization") token: String): Call<List<Message>>
}