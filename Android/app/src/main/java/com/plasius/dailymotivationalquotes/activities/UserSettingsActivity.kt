package com.plasius.dailymotivationalquotes.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plasius.dailymotivationalquotes.R
import com.plasius.dailymotivationalquotes.model.PatchRequest
import com.plasius.dailymotivationalquotes.model.StatusResponse
import com.plasius.dailymotivationalquotes.model.ValidateResponse
import com.plasius.dailymotivationalquotes.restapi.ApiClient
import kotlinx.android.synthetic.main.activity_user_settings.*
import com.plasius.dailymotivationalquotes.restapi.SessionManager
import retrofit2.*

class UserSettingsActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        sessionManager = SessionManager(this)
        apiClient = ApiClient()
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

    fun onDeleteClicked(view: View) {
        val user = sessionManager.fetchUser()
        if(user== null)
            return
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.user_delete)
            .setPositiveButton(R.string.user_delete,
                DialogInterface.OnClickListener { dialog, id ->
                    //delete user here
                    apiClient.getApiService().deleteUser(user.userId,"Bearer ${sessionManager.fetchAuthToken()}")
                        .enqueue(object : Callback<StatusResponse> {
                            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                                // Error fetching posts
                            }

                            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                                if(response.body()?.status == "success"){
                                    Toast.makeText(baseContext, "Profile deleted.", Toast.LENGTH_SHORT).show()
                                    onLogoutClicked(null)
                                }else{
                                    Toast.makeText(baseContext, "Failed to delete user.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } );
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.create().show()
    }


    fun onUpdateClicked(view: View){
        //get user data, update necessary data, send to server
        val user = sessionManager.fetchUser()

        if(user == null) {
            onLogoutClicked(null);
            return
        }

        val request = PatchRequest()

        //validate input
        if(view.tag=="email"){
            request.email = settings_et_email.text.toString()
            settings_et_email.text.clear()
        }else if(view.tag=="username"){
            request.username = settings_et_username.text.toString()
            settings_et_username.text.clear()
        }else { //password
            request.password = settings_et_password.text.toString()
            settings_et_password.text.clear()
        }


        apiClient.getApiService().updateUser(user.userId,"Bearer ${sessionManager.fetchAuthToken()}", request).enqueue(object : Callback<StatusResponse>{
            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                // Error fetching posts
            }
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                Toast.makeText(baseContext, response.body()?.status, Toast.LENGTH_SHORT).show()
                apiClient.getApiService().validateToken("Bearer ${sessionManager.fetchAuthToken()}")
                    .enqueue(object : Callback<ValidateResponse> {
                        override fun onFailure(call: Call<ValidateResponse>, t: Throwable) {
                            // Error fetching posts
                        }

                        override fun onResponse(call: Call<ValidateResponse>, response: Response<ValidateResponse>) {
                            Log.e("API", "2 - Response: ${response.body().toString()}")
                            if(response.body()?.status != null){
                                sessionManager.saveUser(response.body()!!.user)
                                finish()
                            }
                        }
                    })
            }
        });

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
