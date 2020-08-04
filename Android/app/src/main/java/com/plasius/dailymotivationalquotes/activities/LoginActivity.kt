package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.plasius.dailymotivationalquotes.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import java.util.*
import kotlin.time.days


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var lastLogin = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)
        var today = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)

       Log.d("logincct", (today - lastLogin ).toString())

        setContentView(R.layout.activity_login)

    }

    fun updateUI(succes: Boolean){
        if (succes){
                getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", GregorianCalendar.getInstance().get(GregorianCalendar.DATE)).apply()

                val intent=Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
        }else{
            Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
        }
    }

    fun trig_signup(view: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }
        updateUI(true)
    }

    fun trig_signin(view: View) {
        var email = et_email.text.toString()
        var password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }
        updateUI(true)
    }

    fun googleSign(view: View) {

    }

    fun resPass(view: View) {
        var emailAddress = et_email.text.toString()

        if (emailAddress == "") {
            updateUI(false)
            return
        }
    }
}
