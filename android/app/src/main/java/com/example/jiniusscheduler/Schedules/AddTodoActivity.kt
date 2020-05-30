package com.example.jiniusscheduler.Schedules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jiniusscheduler.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddTodoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        auth = Firebase.auth
        database = Firebase.database.reference

        checkAuth()

        findViewById<FloatingActionButton>(R.id.addTodoSaveFAB).setOnClickListener {
            addTodo()
        }
    }

    private fun addTodo() {
        val mySimpleTodoReference =
            database.child("Users/${auth.currentUser!!.uid}/Schedules/SimpleTodo")

        val key = mySimpleTodoReference.push().key

        val newTodo = SimpleTodo(
            auth.currentUser!!.uid,
            findViewById<TextView>(R.id.addToddContentText).text.toString(),
            System.currentTimeMillis()
        )

        val updates = HashMap<String, Any?>()
        updates["$key"] = newTodo

        mySimpleTodoReference.updateChildren(updates)

        finish()

//        myTodoReference.keepSynced(true)
    }

    private fun checkAuth() {
        if (auth.currentUser == null) {
            finish()
        }
    }
}
