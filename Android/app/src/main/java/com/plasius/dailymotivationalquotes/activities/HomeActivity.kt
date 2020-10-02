package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.Quote
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import com.plasius.dailymotivationalquotes.restapi.SessionManager
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        greeter()
        displayQuote()
    }

    private fun greeter(){
        // lol maybe this shouldn't be a separate function
        val greet= arrayOf("Welcome", "Hi", "Greetings,", "Hello there, General", "I did not. Oh, hi")  //add memes
        val curGreet = greet.random()

        val user = sessionManager.fetchUser()
        var name = "Guest"
        if(user != null) {
            if (user.firstName != "") {
                name = user.firstName
            } else {
                name = user.username
            }
        }

        textGreeter.text = "$curGreet $name!"
    }


    //QUOTE DISPAY
    private fun fetchQuote() {
        val randomId = (1..59).random()

        // Pass the token as parameter
        apiClient.getApiService().fetchQuote(randomId/*"Bearer ${sessionManager.fetchAuthToken()}"*/)
            .enqueue(object : Callback<Quote> {
                override fun onFailure(call: Call<Quote>, t: Throwable) {
                    // Error fetching posts
                }

                override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                    // Handle function to display posts
                    textQuote.text = response.body()!!.text
                    getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putString("Quote", response.body()!!.text).apply()

                    getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastFetch", GregorianCalendar.getInstance().get(GregorianCalendar.DATE)).apply()

                    //Toast.makeText(baseContext, response.body()!!.text, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun displayQuote(){
        if(wasActiveToday()){
            textQuote.text = getSharedPreferences("localdata", Context.MODE_PRIVATE).getString("Quote", "")
            return
        }

        fetchQuote()
    }

    //checks if today's date has already been stored as local data
    private fun wasActiveToday():Boolean{
        val calendar = GregorianCalendar.getInstance()
        val today = calendar.get(GregorianCalendar.DATE)
        val lastDay = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastFetch", -1)

        return lastDay == today
    }



    //ANDROID LIFECYCLE
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == R.id.settings){
            val intent= Intent(this, UserSettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        else{
           return super.onOptionsItemSelected(item!!)
        }

    }

    fun trigShare(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textQuote.text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }


    override fun onResume() {
        super.onResume()
        greeter()
    }


}
