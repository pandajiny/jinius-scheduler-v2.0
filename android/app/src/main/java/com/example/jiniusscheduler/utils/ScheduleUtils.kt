package com.example.jiniusscheduler.utils

import com.example.jiniusscheduler.database.DataListenerCallBack
import com.example.jiniusscheduler.database.SaveDataCallBack
import com.example.jiniusscheduler.schedules.todo.Todo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ScheduleUtils {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    private fun todoRef(): DatabaseReference {
        return database.child("${auth.currentUser!!.uid}/Schedules/Todos")
    }

    init {
        todoRef().keepSynced(true)
    }

    //    data : item's list for rv
    fun getSchedules(data: ArrayList<Todo>, callBack: DataListenerCallBack<ArrayList<Todo>>) {
        todoRef().addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                val changed = snapshot.getValue<Todo>()
                if (changed is Todo) {
                    data.remove(data.find { todo -> todo.key == changed.key })
                    data.add(changed)
                    callBack.onChanged(data)
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                val added = snapshot.getValue<Todo>()
                if (added is Todo) {
                    data.add(added)
                    callBack.onChanged(data)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val deleted = snapshot.getValue<Todo>()
                if (deleted is Todo) {
                    data.remove(data.find { todo -> todo.key == deleted.key })
                    callBack.onChanged(data)
                }
            }
        })
    }

    fun addTodo(todo: Todo, callBack: SaveDataCallBack<Todo>) {
        val key = todoRef().push().key
        if (key != null) {

            todo.uid = auth.currentUser!!.uid
            todo.key = key

            val updates = hashMapOf<String, Any?>(
                key to todo
            )
            todoRef().updateChildren(updates).addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onCanceled("Cannot add Todo")
                } else {
                    callBack.onSuccess()
                }
            }
        }
    }

    fun toggleDone(key: String, prevValue: Boolean) {
        todoRef().child("${key}/done").setValue(!prevValue)
    }
}