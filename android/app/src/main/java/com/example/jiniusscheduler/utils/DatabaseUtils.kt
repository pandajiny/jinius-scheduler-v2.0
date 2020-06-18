package com.example.jiniusscheduler.utils

import com.example.jiniusscheduler.schedules.todo.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface GetDataCallBack<T> {
    fun onDataReceive(data: ArrayList<T>)
}

interface SaveDataCallBack {
    fun onSuccess()
    fun onErrorOccurred(errorMessage: String)
}

class DatabaseUtils() {
    private val database = Firebase.database.reference

    fun getSchedules(uid: String, getDataCallBack: GetDataCallBack<Todo>) {
        val schedulesReference = database.child("Users/${uid}/Schedules")
        schedulesReference.keepSynced(true)

        schedulesReference.child("Todo").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    fun addTodo(path: String, todo: Todo, saveDataCallBack: SaveDataCallBack) {
        val key = database.child(path).push().key
        if (key == null) {
            saveDataCallBack.onErrorOccurred("Can't get key from $path")
        } else {
            val updates = mapOf<String, Any?>(
                "$path/$key" to todo
            )
            database.updateChildren(updates).addOnCompleteListener {
                if (it.isSuccessful) {
                    saveDataCallBack.onSuccess()
                } else {
                    saveDataCallBack.onErrorOccurred("Can't not add Todo object to db ${todo.content}")
                }
            }
        }
    }
}