package com.example.jiniusscheduler.utils

import android.util.Log
import android.util.Patterns
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase

class AuthUtils() {
    interface ActionCallBack {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

    interface TokenCallBack {
        fun onGetToken(token: String)
    }

    private val auth = Firebase.auth

    val user: FirebaseUser?
        get() {
            return auth.currentUser
        }

    val uid: String?
        get() {
            return auth.currentUser?.uid
        }

    val email: String?
        get() {
            return auth.currentUser?.email
        }

    fun checkAuth(callBack: ActionCallBack) {
        val user = auth.currentUser
        if (user is FirebaseUser) {
            callBack.onSuccess(user.uid)
        } else {
            callBack.onFailure("Please Login first")
        }
    }

    fun checkAuth(): Boolean {
        val user = auth.currentUser
        return user is FirebaseUser
    }

    fun checkEmailVerification(callBack: ActionCallBack) {
//        check user logged in
        if (user is FirebaseUser) {
            user!!.reload().addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onFailure("can not load user data please login again")
                    return@addOnCompleteListener
                }
//            check user logged in with verified email
                if (user!!.isEmailVerified) {
                    callBack.onSuccess("email verified")
                    return@addOnCompleteListener
                } else {
                    callBack.onFailure("not verified Email :${user!!.email}")
                }
            }
        } else {
            callBack.onFailure("Please Login first")
        }
    }


    fun sendEmailVerification(email: String, callBack: ActionCallBack) {
//        check Auth and check email is not verified
        if (checkAuth() || !auth.currentUser!!.isEmailVerified) {
            auth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onFailure("fail to send verification mail to $email: " + it.exception?.message)
                    return@addOnCompleteListener
                }
                callBack.onSuccess("Verification mail is send to $email")
            }
        } else {
            callBack.onFailure("please login first or check verification")
        }
    }

    fun doLogin(email: String, password: String, callBack: ActionCallBack) {
        if (!emailValidation(email)) {
            callBack.onFailure("please check email form")
            return
        } else if (!passwordValidation(password)) {
            callBack.onFailure("please check password form")
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onFailure("can not do login: " + it.exception?.message)
                return@addOnCompleteListener
            }
            callBack.onSuccess("Successfully Logged In!: " + auth.currentUser!!.uid)
        }
    }

    fun doSignOut(callBack: ActionCallBack) {
        auth.signOut()
        if (auth.currentUser == null) {
            callBack.onSuccess("Successfully signed out!")
        } else {
            callBack.onFailure("cannot do sign out, please try again")
        }
    }


    fun doSignUp(email: String?, password: String?, callBack: ActionCallBack) {
        if (emailValidation(email) and passwordValidation(password)) {
            auth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onFailure("can not create account, please try again: " + it.exception?.message)
                    return@addOnCompleteListener
                }
                sendEmailVerification(email, object : ActionCallBack {
                    override fun onSuccess(message: String) {
                        callBack.onSuccess("Successfully created your account and $message")
                    }

                    override fun onFailure(message: String) {
                        callBack.onSuccess("We created your account but has problem with sending email verification, please login first")
                    }

                })
            }
        } else {
            callBack.onFailure("please check password validation")
        }
    }


    fun getToken(callBack: TokenCallBack) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Instance", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                if (token != null) {
                    callBack.onGetToken(token)
                }
            })
    }

    fun emailValidation(email: String?, callBack: ActionCallBack) {
        when {
            email.isNullOrEmpty() -> {
                callBack.onFailure("please fill email form")
            }
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                callBack.onSuccess("it's validated")
            }
            else -> {
                callBack.onFailure("it's not assign with email form")
            }
        }
    }

    fun emailValidation(email: String?): Boolean {
        return when {
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun passwordValidation(password: String?): Boolean {
        if (!password.isNullOrEmpty()) {
            if (password.count() > 6) {
                if (!password.contains("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$".toRegex())) {
                    return true
                }
            }
        }
        return false
    }

}