package com.plasius.dailymotivationalquotes.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.Transliterator.getDisplayName
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    }

    fun trigLogout(view: View?) {
        auth.signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun trigFeedback(view: View) {
        val email = arrayOf("studios.plotberry@gmail.com")
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
                    trigLogout(null)
                }
            }
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
                    trigLogout(null)
                }
            }
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
        val username = nUsername.text.toString()

        val user = auth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, R.string.username_changed, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun updatePass(){
        val user = auth.currentUser
        val password = nPass.text.toString()

        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, R.string.success,Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(baseContext, "Failed",Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun trigUpdatePass(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Change password?")
            .setPositiveButton(R.string.save,
                DialogInterface.OnClickListener { dialog, id ->
                    updatePass()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.create().show()

    }


    //MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == R.id.signout){
            trigLogout(null)
            return true
        }else {
            return super.onOptionsItemSelected(item!!)
        }

    }

}
