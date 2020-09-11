package com.plasius.dailymotivationalquotes.restapi

import com.plasius.dailymotivationalquotes.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constants.REGISTER_URL)
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST(Constants.DELETE_USER_ULR)
    fun deleteUser(@Header("Authorization") token:String): Call<String>

    @GET(Constants.QUOTES_URL)
    //fun fetchQuote(@Header("Authorization") token: String): Call<List<Quote>>
    fun fetchQuote(@Path("id") id: Int): Call<Quote>

    @GET(Constants.VALIDATION_URL)
    fun validateToken(@Header("Authorization") token : String) : Call<Int>
}