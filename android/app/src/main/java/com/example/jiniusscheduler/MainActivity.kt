package com.example.jiniusscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jiniusscheduler.Schedules.SchedulesActivity
import com.example.jiniusscheduler.auth.LoginActivity
import com.example.jiniusscheduler.friends.FriendsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        checkAuth()
    }

    private fun checkAuth() {
        if (Firebase.auth.currentUser == null) {
            startLoginPage()
        } else {
            startSchedulesPage()
        }
    }

    private fun startLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun startSchedulesPage() {
        val intent = Intent(this, SchedulesActivity::class.java)
        startActivity(intent)
    }

    private fun startFriendsPage() {
        val intent = Intent(this, FriendsActivity::class.java)
        startActivity(intent)
    }

}
