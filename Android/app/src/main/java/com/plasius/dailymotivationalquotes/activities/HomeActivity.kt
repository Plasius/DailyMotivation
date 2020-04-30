package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.plasius.dailymotivationalquotes.R
import java.util.GregorianCalendar

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(wasActiveToday()){
            val quote = getSharedPreferences("localdata", Context.MODE_PRIVATE).getString("Quote", null)


        }else{
            var quotes = fetchAllQuotes()
            var userQuotes = fetchUserQuotes()

            if(quotes.size == userQuotes.size){
                //you reached the end of the quotes, reset
            }

        }


    }

    private fun wasActiveToday():Boolean{
        val calendar = GregorianCalendar.getInstance()
        val today = calendar.get(GregorianCalendar.DATE)
        val lastDay = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)

        return lastDay == today
    }

    private fun fetchAllQuotes():Array<String>{
        return arrayOf("quote1", "quote2", "quote3")
    }

    private fun fetchUserQuotes():Array<Int>{
        return arrayOf(0,2)
    }

}
