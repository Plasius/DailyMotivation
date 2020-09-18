package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.LoginRequest
import com.plasius.dailymotivationalquotes.model.LoginResponse
import com.plasius.dailymotivationalquotes.model.RegisterRequest
import com.plasius.dailymotivationalquotes.model.RegisterResponse
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import com.plasius.dailymotivationalquotes.restapi.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_email
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
    }

    private fun updateUI(success: Boolean){
        if (success){
            getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", GregorianCalendar.getInstance().get(
                GregorianCalendar.DATE)).apply()

            val intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
        }
    }

    fun login(email: String, password: String) {
        var email = et_email.text.toString()
        var password = et_password.text.toString()

        if(email == "" || password == ""){
            email = "aaa@gg.com"
            password = "12345678"
            //updateUI(false)
            //return
        }

        //USE WITH WORKING REST API SERVER
        apiClient.getApiService().login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse?.status == "success") {
                        sessionManager.saveAuthToken(loginResponse.authToken)
                        sessionManager.saveUser(loginResponse.user)
                        updateUI(true)
                    } else {
                        // Error logging in
                    }
                }
            })

    }

    fun register(view: View) {
        var email = et_email.text.toString()
        var password = et_password.text.toString()
        var username = et_username.text.toString()


        if(email == "" || password == "" || username == ""){
            email = "aaa@gg.com"
            password = "12345678"
            username = "admin"
            //updateUI(false)
            //return
        }


        apiClient.getApiService().register(RegisterRequest(email, password, username, "", ""))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                    val registerResponse = response.body()

                    if (registerResponse?.status == "success" ) {
                        //if success on register, start login
                        login(email, password)
                    } else {
                        // Error logging in
                    }
                }
            })


    }
}