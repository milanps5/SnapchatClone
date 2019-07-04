package com.milanps.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (auth.currentUser != null) {
            logIn()
        }

    }

    fun goClicked(view: View) {
        //Check if we can log in the user
        auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    auth.createUserWithEmailAndPassword(
                        emailEditText?.text.toString(),
                        passwordEditText?.text.toString()
                    ).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Add to database
                            logIn()
                        } else {
                            Toast.makeText(this, "Login Failed, Try Again!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }

            }

        //Sign up the user
    }

    fun logIn() {
        //Move to next Activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)

    }
}
