package com.example.jiniusscheduler.friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jiniusscheduler.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FriendsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        findViewById<TextView>(R.id.friendsMyNameText).text = auth.currentUser!!.email
    }
}
