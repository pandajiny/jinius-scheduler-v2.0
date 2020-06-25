package com.example.jiniusscheduler.database

class Database {
    interface GetDataCallBack<T> {
        fun onDataReceived(data: T)
        fun onCanceled(message: String)
    }

    interface SaveDataCallBack {
        fun onSuccess(message: String)
        fun onCanceled(message: String)
    }

    enum class SCHEDULE_TYPES {
        SIMPLE_TODO,
        SCHEDULED_TODO
    }

    data class SCHEDULE(
        var key: String = "",
        var uid: String = "",
        var type: SCHEDULE_TYPES = SCHEDULE_TYPES.SIMPLE_TODO,
        var title: String = "",
        var requestTimestamp: Long = 0,
        var done: Boolean = false,
//    optional
        var scheduledDateTime: Long? = null
    )
}