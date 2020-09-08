package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.LoginRequest
import com.plasius.dailymotivationalquotes.model.LoginResponse
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import com.plasius.dailymotivationalquotes.restapi.SessionManager
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

        val lastLogin = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)
        val today = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)

        if(today - lastLogin < 5){
            updateUI(true)
        }

        setContentView(R.layout.activity_login)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
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
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }

        //USE WITH WORKING REST API SERVER
        apiClient.getApiService().login(LoginRequest(email = email, password = password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.status == "success" ) {
                        //if success on register, start login
                        login(null)
                    } else {
                        // Error logging in
                    }
                }
            })

        updateUI(true)
    }

    fun login(view: View?) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }

        //USE WITH WORKING REST API SERVER
        apiClient.getApiService().login(LoginRequest(email = email, password = password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.status == "success") {
                        sessionManager.saveAuthToken(loginResponse.auth_token)

                    } else {
                        // Error logging in
                    }
                }
            })

        updateUI(true)
    }


    fun resPass(view: View) {
        val emailAddress = et_email.text.toString()

        if (emailAddress == "") {
            updateUI(false)
            return
        }
    }
}
