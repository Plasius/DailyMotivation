package com.plasius.dailymotivationalquotes.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.Transliterator.getDisplayName
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.plasius.dailymotivationalquotes.R
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.android.synthetic.main.auxact_update_email.*
import com.plasius.dailymotivationalquotes.R.string

class UserSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        auth= FirebaseAuth.getInstance()

        curUser.text=auth.currentUser?.displayName.toString()
    }

    fun trigLogout(view: View) {
        auth.signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun deleteUser(){
        val user = auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext,"Success!",Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else{
                    Toast.makeText(baseContext, R.string.welcome, Toast.LENGTH_LONG).show()
                    logoutButton.callOnClick()
                }
            }
    }

    fun trigDelete(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_user)
            .setPositiveButton(R.string.delete_user,
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
        val user = auth.currentUser

        val newMail = nMail.text.toString()

        Toast.makeText(baseContext, "Changing e-mail to: $newMail", Toast.LENGTH_SHORT).show()

        user!!.updateEmail(newMail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Changed e-mail to: $newMail", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                    logoutButton.callOnClick()
                }
            }
    }

    fun trigUpdateMail(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.c_mail)
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
        val username = nUsername.text.toString()

        val user = auth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, R.string.c_username, Toast.LENGTH_SHORT).show()
                }
            }

    }
}
