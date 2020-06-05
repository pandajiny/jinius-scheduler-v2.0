package com.example.jiniusscheduler.schedules

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.schedules.todo.AddTodoActivity
import com.example.jiniusscheduler.schedules.todo.EditTodoActivity
import com.example.jiniusscheduler.schedules.todo.Todo
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

    private lateinit var scheduleList: ArrayList<Todo>

    private lateinit var viewAdapter: SchedulesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)

        auth = Firebase.auth
        database = Firebase.database.reference

        checkAuth()

        scheduleList = arrayListOf()


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

    override fun onStart() {
        super.onStart()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewAdapter.notifyDataSetChanged()
                handler.postDelayed(this, 1000)//1 sec delay
            }
        }, 0)
    }

    private fun getScheduleList() {
        val myScheduleListReference = database.child("Users/${auth.currentUser!!.uid}/Schedules")

        myScheduleListReference.child("Todo")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                    val todo = dataSnapshot.getValue<Todo>()!!
                    scheduleList.forEach {
                        if (it.key == todo.key) {
                            scheduleList.remove(it)
                            scheduleList.add(todo)
                            scheduleList.sortBy { Todo -> Todo.requestTimestamp }
                            viewAdapter.notifyDataSetChanged()
                            return
                        }
                    }
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    val schedule = dataSnapshot.getValue<Todo>()
                    if (schedule is Todo) {
                        scheduleList.add(schedule)
                        scheduleList.sortBy { Todo -> Todo.requestTimestamp }
                    }
                    viewAdapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val todo = dataSnapshot.getValue<Todo>()
                    if (todo is Todo) {
                        scheduleList.forEach {
                            if (it.key == todo.key) {
                                scheduleList.remove(todo)
                                scheduleList.sortBy { Todo -> Todo.requestTimestamp }
                                viewAdapter.notifyDataSetChanged()
                                return
                            }
                        }
                    }
                }
            })

        myScheduleListReference.keepSynced(true)
    }

    private fun startAddTodoActivity() {
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
    }

    fun startEditTodoActivity(key: String) {
        val intent = Intent(this, EditTodoActivity::class.java).putExtra("key", key)
        startActivity(intent)
    }

    private fun checkAuth() {
        if (auth.currentUser?.uid == null) {
            finish()
        }
    }

    fun checkDone(key: String, value: Boolean) {
        val currentTodoReference =
            database.child("Users/${auth.currentUser!!.uid}/Schedules/Todo/${key}")
        currentTodoReference.child("done").setValue(value)
    }
}
