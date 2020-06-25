package com.example.jiniusscheduler.utils

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsersUtils {
    val db = Firebase.firestore

    fun userRef(uid: String): DocumentReference {
        return db.collection("users").document(uid)
    }
}