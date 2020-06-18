package com.example.jiniusscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jiniusscheduler.schedules.SchedulesActivity
import com.example.jiniusscheduler.auth.LoginActivity
import com.example.jiniusscheduler.utils.AuthCallBack
import com.example.jiniusscheduler.utils.AuthUtils
import com.example.jiniusscheduler.utils.DatabaseUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        AuthUtils(object : AuthCallBack {
            override fun onRequireAuth() {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            override fun onGetAuth() {
                startActivity(Intent(this@MainActivity, SchedulesActivity::class.java))
                finish()
            }
        })


    }
}
