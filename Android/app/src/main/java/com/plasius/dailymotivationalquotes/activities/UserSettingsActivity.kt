package com.plasius.dailymotivationalquotes.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.Transliterator.getDisplayName
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.plasius.dailymotivationalquotes.R
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.android.synthetic.main.auxact_update_email.*
import com.plasius.dailymotivationalquotes.R.string
import com.plasius.dailymotivationalquotes.model.User
import com.plasius.dailymotivationalquotes.restapi.SessionManager

class UserSettingsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        sessionManager = SessionManager(this)

    }


    fun onFeedbackClicked(view: View) {
        val email = arrayOf("supp.plotberry@gmail.com")
        val subject = "DMQ - Feedback"
        val message = ""

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            type = "message/rfc822"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Send email using:")
        startActivity(shareIntent)

    }

    fun deleteUser(){

    }

    fun trigDelete(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.user_delete)
            .setPositiveButton(R.string.user_delete,
                DialogInterface.OnClickListener { dialog, id ->
                    deleteUser()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.create().show()


    }

    fun updateMail(){

    }

    fun trigUpdateMail(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.email_change)
            .setPositiveButton(R.string.save,
                DialogInterface.OnClickListener { dialog, id ->
                    updateMail()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.create().show()

    }

    fun trigUpdateUser(view: View) {
        val username = settings_et_username.text.toString()

    }

    fun onUpdateClicked(view: View){
        //get user data, update necessary data, send to server
        val user = sessionManager.fetchUser()

        if(user == null)
            onLogoutClicked(null);

        //validate token

        //validate input
        if(view.tag=="email"){
            user?.email = settings_et_email.text.toString();
        }else if(view.tag=="username"){
            user?.username = settings_et_username.text.toString();
        }else{ //password
            user?.password = settings_et_password.text.toString();
        }

        //send to server

        //if successful, save


    }


    fun onLogoutClicked(view: View?) {
        getSharedPreferences("localdata", Context.MODE_PRIVATE).edit().putInt("lastDay", 0).apply()
        sessionManager.saveAuthToken("")

        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun onRateClicked(view: View) {
        val sendIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.plasius.dailymotivationalquotes"))
        startActivity(sendIntent)
    }

}
