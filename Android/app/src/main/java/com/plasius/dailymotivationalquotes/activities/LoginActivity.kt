package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import java.util.*
import kotlin.time.days


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lastLogin = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)
        val today = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)

        if(today - lastLogin < 5){
            updateUI(true)
        }

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
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email == "" || password == ""){
            updateUI(false)
            return
        }
        updateUI(true)
    }

    fun googleSign(view: View) {

    }

    fun resPass(view: View) {
        val emailAddress = et_email.text.toString()

        if (emailAddress == "") {
            updateUI(false)
            return
        }
    }
}
