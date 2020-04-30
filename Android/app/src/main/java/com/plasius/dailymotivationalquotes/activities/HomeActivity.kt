package com.plasius.dailymotivationalquotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.plasius.dailymotivationalquotes.R

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


    }

    private fun fetchQuotes(){
        
    }

    fun signOut(view: View) {
        auth.signOut()
        finish()

    }
}
