package com.example.jiniusscheduler.database

interface SaveDataCallBack<T> {
    fun onSuccess()
    fun onCanceled(message: String)
}

interface DataListenerCallBack<T> {
    fun onChanged(data: T)
}