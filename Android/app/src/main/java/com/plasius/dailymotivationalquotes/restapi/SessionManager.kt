package com.plasius.dailymotivationalquotes.restapi

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.User

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER = "user"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUser(user: User){
        val editor = prefs.edit()
        val jsonString = GsonBuilder().create().toJson(user)
        editor.putString(USER, jsonString)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUser(): User?{
        val string = prefs.getString(USER, null)

        if(string != null){
        val user = GsonBuilder().create().fromJson(string, User::class.java)
        return user
        }else{
            return null
        }
    }
}