package com.example.jiniusscheduler.Schedules

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
class SimpleTodo(var uid: String = "", var content: String = "", var requestTimestamp: Long = 0) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "content" to content,
            "requestTimestamp" to requestTimestamp
        )
    }
}