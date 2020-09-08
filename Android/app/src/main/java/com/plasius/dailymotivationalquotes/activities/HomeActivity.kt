package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.Message
import com.plasius.dailymotivationalquotes.model.Quote
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import com.plasius.dailymotivationalquotes.restapi.SessionManager
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ThreadLocalRandom


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

        textGreeter.text = "$curGreet User!"
    }

    private fun fetchQuote() {

        // Pass the token as parameter
        apiClient.getApiService().fetchQuote("Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<List<Quote>> {
                override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                    // Error fetching posts
                }

                override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>
                ) {
                    // Handle function to display posts
                    Toast.makeText(baseContext, response.body()!![0].text, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun displayQuote(){
        fetchQuote()
    }

    //checks if today's date has already been stored as local data
    private fun wasActiveToday():Boolean{
        val calendar = GregorianCalendar.getInstance()
        val today = calendar.get(GregorianCalendar.DATE)
        val lastDay = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("lastDay", -1)

        return lastDay == today
    }

    //retrieves all quotes based on locale from the Firebase Realtime database
    private fun fetchAllQuotes(locale:String="en"){

    }

    //retrieves the user-specific id-list of accessed quotes
    private fun fetchUserQuotes(){

    }

    private fun trigLogout() {
        getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", 0).apply()

        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

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
        }else if(item?.getItemId() == R.id.fav){
            val intent= Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            return true
        }else if(item?.getItemId() == R.id.signout){
            trigLogout()
            return true
        }else{
           return super.onOptionsItemSelected(item!!)
        }

    }

    fun trigShare(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Coming soon...")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }



}
