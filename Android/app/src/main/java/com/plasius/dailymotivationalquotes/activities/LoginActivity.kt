package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.*
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import com.plasius.dailymotivationalquotes.restapi.SessionManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.time.days


class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        Log.e("API","1")
        //check if token works, if so, update UI
        apiClient.getApiService().validateToken("Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<ValidateResponse> {
                override fun onFailure(call: Call<ValidateResponse>, t: Throwable) {
                    // Error fetching posts
                }

                override fun onResponse(call: Call<ValidateResponse>, response: Response<ValidateResponse>) {
                    Log.e("API", "2 - Response: ${response.body().toString()}")
                    if(response.body()?.status != null){
                        sessionManager.saveUser(response.body()!!.user)
                        updateUI(true)
                    }
                }
            })

        setContentView(R.layout.activity_login)

    }

    private fun updateUI(success: Boolean){
        if (success){
                getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", GregorianCalendar.getInstance().get(GregorianCalendar.DATE)).apply()

                val intent=Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
        }else{
            Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
        }
    }

    fun register(view: View) {
        val intent=Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(view: View) {
        var email = et_email.text.toString()
        var password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }

        //USE WITH WORKING REST API SERVER
        apiClient.getApiService().login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                    Toast.makeText(baseContext, "Error logging in.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()
                    Log.e("API", "3 - Response: ${response.body().toString()}")
                    if (loginResponse?.status == "success") {
                        sessionManager.saveAuthToken(loginResponse.authToken)
                        sessionManager.saveUser(loginResponse.user)
                        updateUI(true)
                    } else {
                        // Error logging in
                        Toast.makeText(baseContext, "Error logging in.", Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }


    fun resPass(view: View) {
        register(view)
    }
}
