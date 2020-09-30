package com.plasius.dailymotivationalquotes.restapi

import com.plasius.dailymotivationalquotes.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constants.REGISTER_URL)
    fun register(@Body request: RegisterRequest): Call<StatusResponse>

    @DELETE(Constants.USER_URL)
    fun deleteUser(@Path("id") id : Int, @Header("Authorization") token:String): Call<StatusResponse>

    @GET(Constants.QUOTES_URL)
    fun fetchQuote(@Path("id") id: Int): Call<Quote>

    @GET(Constants.VALIDATION_URL)
    fun validateToken(@Header("Authorization") token : String) : Call<ValidateResponse>

    @PATCH(Constants.USER_URL)
    fun updateUser(@Path("id") id : Int, @Header("Authorization") token : String, @Body request : PatchRequest ) : Call<StatusResponse>
}