package com.example.jiniusscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jiniusscheduler.schedules.SchedulesActivity
import com.example.jiniusscheduler.auth.LoginActivity
import com.example.jiniusscheduler.utils.AuthUtils

class InitializeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        AuthUtils().checkAuth(object : AuthUtils.ActionCallBack {
            override fun onSuccess(message: String) {
                startActivity(
                    Intent(
                        this@InitializeActivity, SchedulesActivity::class.java
                    )
                )
                finish()
            }

            override fun onFailure(message: String) {
                startActivity(Intent(this@InitializeActivity, LoginActivity::class.java))
                finish()
            }
        })
    }
}
