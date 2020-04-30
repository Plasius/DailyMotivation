package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import java.util.GregorianCalendar
import java.util.concurrent.ThreadLocalRandom

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(wasActiveToday()){
            val quote = getSharedPreferences("localdata", Context.MODE_PRIVATE).getString("quote", null)
            Toast.makeText(baseContext, "quote loaded: $quote", Toast.LENGTH_LONG).show()

        }else{
            val quotes = fetchAllQuotes()
            val userQuotes = fetchUserQuotes()

            if(quotes.size == userQuotes.size){
                //you reached the end of the quotes, reset firebase userQuotes
            }

            val randomRange = quotes.size - userQuotes.size
            val random = ThreadLocalRandom.current().nextInt(0, randomRange)
            var counter = -1
            for(i in 0..quotes.size){
                if(i in userQuotes){
                    continue
                }else{
                    counter++
                    if(counter == random){
                        //we found our next quote at i
                        //save our new quote and return
                        getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putString("quote", quotes[i]).apply()
                        getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", GregorianCalendar.getInstance().get(GregorianCalendar.DATE)).apply()
                        return
                    }
                }
            }
        }

    }

    //checks if the today's date has already been stored as local data
    private fun wasActiveToday():Boolean{
        val calendar = GregorianCalendar.getInstance()
        val today = calendar.get(GregorianCalendar.DATE)
        val lastDay = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)

        return lastDay == today
    }

    //retrieves all quotes based on locale from the Firebase Realtime database
    private fun fetchAllQuotes():Array<String>{
        return arrayOf("quote1", "quote2", "quote3", "quote4", "quote5", "quote6")
    }

    //retrieves the user-specific id-list of accessed quotes
    private fun fetchUserQuotes():Array<Int>{
        return arrayOf(0,1,3,4)
    }

    fun onSettingsClicked(view: View) {
        val intent= Intent(this, UserSettingsActivity::class.java)
        startActivity(intent)
    }

}
