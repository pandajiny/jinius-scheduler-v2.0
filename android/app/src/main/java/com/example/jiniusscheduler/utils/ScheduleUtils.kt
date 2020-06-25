package com.example.jiniusscheduler.utils

import com.example.jiniusscheduler.database.DataListenerCallBack
import com.example.jiniusscheduler.database.SaveDataCallBack
import com.example.jiniusscheduler.database.Database
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ScheduleUtils {
    //    private val database = Firebase.database.reference
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun schedulesRef(): CollectionReference {
        return UsersUtils().userRef(AuthUtils().uid!!).collection("schedules")
    }

    fun scheduleRef(id: String): DocumentReference {
        return schedulesRef().document(id)
    }

    //    data : item's list for rv
    fun getActivateSchedules(
        callBack: Database.GetDataCallBack<List<Database.SCHEDULE>>
    ) {
        schedulesRef().whereEqualTo("done", false).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("cannot get schedules")
                return@addOnCompleteListener
            }
            val schedules = it.result?.toObjects<Database.SCHEDULE>()
            if (schedules is List<Database.SCHEDULE>) {
                callBack.onDataReceived(schedules)
            } else {
                callBack.onCanceled("cannot read data")
            }
        }
    }

    fun getSchedule(key: String, callBack: Database.GetDataCallBack<Database.SCHEDULE>) {
        schedulesRef().document(key).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("cannot get Schedule with key :$key")
                return@addOnCompleteListener
            }
            val schedule = it.result?.toObject<Database.SCHEDULE>()
            if (schedule is Database.SCHEDULE) {
                callBack.onDataReceived(schedule)
            } else {
                callBack.onCanceled("${it.exception?.message}")
            }
        }
    }

    fun saveTodo(
        provided_key: String?,
        todo: Database.SCHEDULE,
        callBack: SaveDataCallBack<Database.SCHEDULE>
    ) {
        var key = provided_key
        if (provided_key == null) {
            key = schedulesRef().document().id
        }
        if (key != null) {
            todo.uid = auth.currentUser!!.uid
            todo.key = key

            schedulesRef().document(key).set(todo).addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onCanceled("cannot save todo data: ${it.exception}")
                    return@addOnCompleteListener
                }
                callBack.onSuccess()
            }
        }
    }

    fun toggleDone(id: String, prevValue: Boolean) {
        scheduleRef(id).update("done", !prevValue)
    }
}