package com.milanps.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import jdk.nashorn.internal.runtime.ECMAErrors.getMessage
import com.google.firebase.FirebaseError
import com.google.firebase.database.DatabaseError


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    // lateinit var database: DatabaseReference


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
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user.uid)
                                .child("email").setValue(emailEditText?.text.toString())
                            Toast.makeText(baseContext, "Signup successful", Toast.LENGTH_SHORT).show()

//                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user.uid)
//                                .child("email").setValue(emailEditText!!.text.toString(),
//                                    object : DatabaseReference.CompletionListener {
//                                        override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
//                                            if (p0 != null) {
//                                                System.out.println("Data could not be saved. " + p0.toString())
//                                            } else {
//                                                println("Data saved successfully.")
//                                            }
//                                        }
//                                    })

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
