package com.example.jiniusscheduler.schedules

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.database.DataListenerCallBack
import com.example.jiniusscheduler.database.Database
import com.example.jiniusscheduler.schedules.todo.EditTodoActivity
import com.example.jiniusscheduler.utils.ScheduleUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_schedules.*
import org.jetbrains.anko.toast

class SchedulesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.scheduleListContainer, ScheduleListFragment())
                .commit()
        }

        schedulesAddFAB.setOnClickListener {
            startActivity(Intent(this, EditTodoActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun startEditTodoActivity(key: String) {
        val intent = Intent(this, EditTodoActivity::class.java).putExtra("key", key)
        startActivity(intent)
    }
}
