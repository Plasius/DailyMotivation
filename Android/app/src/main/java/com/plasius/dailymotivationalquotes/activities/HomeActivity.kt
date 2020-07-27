package com.plasius.dailymotivationalquotes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.plasius.dailymotivationalquotes.R
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class HomeActivity : AppCompatActivity() {
    private var quotes : List<String> = listOf()
    private var userQuotes : List<Int> = listOf()
    private var userFav: List<Int> = listOf()
    private var quotesLoaded = false
    private var userQuotesLoaded = false
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val displayName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
    var currentQuote = ""
    var currentQuoteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        greeter()
        displayQuote()
    }

    private fun greeter(){
        // lol maybe this shouldn't be a separate function
        val greet= arrayOf("Welcome", "Hi", "Greetings,", "Hello there, General", "I did not. Oh, hi")  //add memes
        val curGreet = greet.random()

        textGreeter.text = "$curGreet $displayName!"
    }

    private fun displayQuote(){
        if(wasActiveToday()){
            val quote = getSharedPreferences("localdata", Context.MODE_PRIVATE).getString("quote", null)
            currentQuoteId = getSharedPreferences("localdata", Context.MODE_PRIVATE).getInt("quoteID", 0)
            //Toast.makeText(baseContext, "quote loaded: $quote", Toast.LENGTH_LONG).show()
            textQuote.text = quote
            currentQuote = quote.toString()

        }else{

            if(!quotesLoaded && !userQuotesLoaded){

                fetchAllQuotes()
                fetchUserQuotes()
                return
            }else if(!quotesLoaded || !userQuotesLoaded)
                return

            //we may now work with the database
            val reference = Firebase.database.reference

            //if the user saw everything
            if(quotes.size == userQuotes.size){
                reference.child("userdata").child(uid).child("quotes").removeValue()
            }

            //if there are no previous quotes, initialize first quote
            var quoteId = -1
            if(userQuotes.isEmpty()){
                quoteId = ThreadLocalRandom.current().nextInt(0, quotes.size)

            }else {  //generate a random quote position and find it, while ignoring the already displayed quotes
                val randomRange = quotes.size - userQuotes.size
                val random = ThreadLocalRandom.current().nextInt(0, randomRange)
                var counter = -1
                for (i in 0..quotes.size) {
                    if (i in userQuotes) {
                        continue
                    } else {
                        counter++
                        if (counter == random) {
                            //we found our next quote at i
                            quoteId = i
                            break
                        }
                    }
                }
            }

            //save our quote for future use
            Firebase.database.getReference("userdata").child(uid).child("quotes").child(userQuotes.size.toString()).setValue(quoteId)

            getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putString("quote", quotes[quoteId]).apply()
            getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("quoteID", quoteId).apply()
            getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", GregorianCalendar.getInstance().get(GregorianCalendar.DATE)).apply()

            //Toast.makeText(baseContext, "quote loaded: ${quotes[quoteId]}", Toast.LENGTH_LONG).show()
            textQuote.text = quotes[quoteId]
            currentQuote = quotes[quoteId]
            currentQuoteId = quoteId
        }
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
        val reference = Firebase.database.getReference("quotes/$locale")

        // Read from the database
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                quotes = dataSnapshot.getValue<List<String>>()!!
                quotesLoaded = true
                displayQuote()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to access database quotes", Toast.LENGTH_LONG).show()
            }
        })
    }

    //retrieves the user-specific id-list of accessed quotes
    private fun fetchUserQuotes(){
        val reference = Firebase.database.getReference("userdata/$uid/quotes")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val temp = dataSnapshot.getValue<List<Int>>()
                if(temp==null){
                    userQuotes = listOf()
                }else{
                    userQuotes = temp
                }

                userQuotesLoaded = true
                displayQuote()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, getString(R.string.something_wrong), Toast.LENGTH_LONG).show()
            }
        })
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
        }else {
           return super.onOptionsItemSelected(item!!)
        }

    }

    fun trigShare(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, currentQuote)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    private fun fetchUserFavourite(){
        val reference = Firebase.database.getReference("userdata/$uid/favourite")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val temp = dataSnapshot.getValue<List<Int>>()
                if(temp==null){
                    userFav = listOf()
                }else{
                    userFav = temp
                }

               // display stuff

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, getString(R.string.something_wrong), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addFav(){
        Firebase.database.getReference("userdata").child(uid).child("favourite").child(userFav.size.toString()).setValue(currentQuoteId)
        Toast.makeText(baseContext, "Added $currentQuoteId to favourite", Toast.LENGTH_LONG).show()
    }

    fun removeFav(){
        Toast.makeText(baseContext, "Coming soon...", Toast.LENGTH_LONG).show()
    }

    fun isFavourite(): Boolean {
        for(n in userFav){
            if(n == currentQuoteId)
                return true
        }
        return false
    }

    fun trigFav(view: View) {
        fetchUserFavourite()

        if(!isFavourite()){
            addFav()
        }else{
            removeFav()
        }
    }

}
