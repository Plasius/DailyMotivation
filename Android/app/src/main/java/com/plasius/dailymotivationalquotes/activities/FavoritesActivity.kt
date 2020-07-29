package com.plasius.dailymotivationalquotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.plasius.dailymotivationalquotes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {
    private var quotes : List<String> = listOf()
    private var userFav: List<Int> = listOf()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        fetchAllQuotes()
        fetchUserFavourite()
        displayFavourite()
    }

    private fun displayFavourite(){
        val textv = TextView(this)
        textv.text = "test"
        mainLayout.addView(textv)
        for(i in userFav){
            val tv = TextView(this)
            tv.text = quotes[i]
            mainLayout.addView(tv)
        }
    }

    private fun fetchAllQuotes(locale:String="en"){
        val reference = Firebase.database.getReference("quotes/$locale")

        // Read from the database
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                quotes = dataSnapshot.getValue<List<String>>()!!
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to access database quotes", Toast.LENGTH_LONG).show()
            }
        })
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
}
