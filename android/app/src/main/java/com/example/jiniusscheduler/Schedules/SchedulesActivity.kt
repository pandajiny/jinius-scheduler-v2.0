package com.example.jiniusscheduler.Schedules

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jiniusscheduler.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SchedulesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var scheduleList: ArrayList<Any>

    private lateinit var viewAdapter: SchedulesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)

        auth = Firebase.auth
        database = Firebase.database.reference

        checkAuth()

        scheduleList = arrayListOf<Any>()


        viewAdapter = SchedulesListAdapter(this, scheduleList)
        val viewManager = LinearLayoutManager(this)


        findViewById<RecyclerView>(R.id.schedulesListRV).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        getScheduleList()


        findViewById<FloatingActionButton>(R.id.schedulesAddFAB).setOnClickListener {
            startAddTodoActivity()
        }
    }

    private fun getScheduleList() {
        val myScheduleListReference = database.child("Users/${auth.currentUser!!.uid}/Schedules")

        myScheduleListReference.child("SimpleTodo")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    val schedule = dataSnapshot.getValue<SimpleTodo>()
                    if (schedule is SimpleTodo) {
                        scheduleList.add(schedule)
                    }
                    viewAdapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("Not yet implemented")
                }
            })

        myScheduleListReference.keepSynced(true)
    }

    private fun startAddTodoActivity() {
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
    }

    private fun checkAuth() {
        if (auth.currentUser?.uid == null) {
            finish()
        }
    }

}
