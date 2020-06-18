package com.example.jiniusscheduler.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface AuthCallBack {
    fun onRequireAuth()
    fun onGetAuth()
}

class AuthUtils(private val authCallBack: AuthCallBack) {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    init {
        checkAuth()
    }

    private fun checkAuth() {
        if (auth.currentUser == null) {
            authCallBack.onRequireAuth()
        } else {
            authCallBack.onGetAuth()
        }
    }
}