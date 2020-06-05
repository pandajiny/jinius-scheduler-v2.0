package com.example.jiniusscheduler.schedules

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Todo(
    var key: String = "",
    var uid: String = "",
    var type: String = "",
    var content: String = "",
    var requestTimestamp: Long = 0,
    var done: Boolean = false,
//    optional
    var scheduledDateTime: Long? = null
)