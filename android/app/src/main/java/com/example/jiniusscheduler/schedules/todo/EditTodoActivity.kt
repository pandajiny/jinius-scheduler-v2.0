package com.example.jiniusscheduler.schedules.todo

import TimeCallbackListener
import TimePickerFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.schedules.DateCallbackListener
import com.example.jiniusscheduler.schedules.DatePickerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_todo.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class EditTodoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    var currentYear: Int? = null
    var currentMonth: Int? = null
    var currentDayOfMonth: Int? = null

    var currentMinute: Int? = null
    var currentHour: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onStart() {
        super.onStart()
//        check auth
        if (auth.currentUser == null) {
            finish()
        }
//        get data
        val key = intent.extras?.getString("key")
        if (key == null) {
            finish()
        } else {
            getTodo(key)
        }
    }

    private fun getTodo(key: String) {
        val currentTodoReference =
            database.child("Users/${auth.currentUser!!.uid}/Schedules/Todo/${key}")
        currentTodoReference.keepSynced(true)
        currentTodoReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val todo = dataSnapshot.getValue<Todo>()
                if (todo is Todo) {
                    initView(todo)
                }
            }
        })
    }

    private fun initView(todo: Todo) {
//        update ui with todo

//        set content
        editTodoContentText.setText(todo.content)

        if (todo.scheduledDateTime != null) {
            editTodoScheduleCheckBox.isChecked = true
            editTodoDateText.text =
                SimpleDateFormat("yyyy/MM/dd").format(Date(todo.scheduledDateTime!!))
            val hours = todo.scheduledDateTime!! / 1000 / 3600 % 24
            val minutes = todo.scheduledDateTime!! / 1000 / 60 % 60
            editTodoTimeText.text = "${hours}:${minutes}"
        }

        editTodoDateText.isEnabled = editTodoScheduleCheckBox.isChecked
        editTodoTimeText.isEnabled = editTodoScheduleCheckBox.isChecked

        editTodoScheduleCheckBox.setOnClickListener {
            editTodoDateText.isEnabled = editTodoScheduleCheckBox.isChecked
            editTodoTimeText.isEnabled = editTodoScheduleCheckBox.isChecked
        }


//        when click date text
        editTodoDateText.setOnClickListener {
//            start dialog fragment with call back
            val dateCallbackListener = object :
                DateCallbackListener {
                override fun onDataReceived(year: Int, month: Int, dayOfMonth: Int) {
                    currentYear = year - 1900
                    currentMonth = month
                    currentDayOfMonth = dayOfMonth
                    editTodoDateText.text = "${year}/${month + 1}/${dayOfMonth}"
                }
            }
            DatePickerFragment(
                dateCallbackListener
            ).show(supportFragmentManager, "timePicker")
        }
//        when click time text
        editTodoTimeText.setOnClickListener {
//            start dialog fragment with call back
            val timeCallbackListener = object :
                TimeCallbackListener {
                override fun onDataReceived(hourOfDay: Int, minute: Int) {

                    currentMinute = minute
                    currentHour = hourOfDay

                    val time_text = "${if (currentHour!! < 10) {
                        "0${currentHour}"
                    } else {
                        currentHour
                    }}:${if (currentMinute!! < 10) {
                        "0${currentMinute}"
                    } else {
                        currentMinute
                    }}:00"
                    editTodoTimeText.text = time_text
                }
            }
            TimePickerFragment(timeCallbackListener).show(supportFragmentManager, "timePicker")
        }

//        set onclick listener
        editTodoDeleteFAB.setOnClickListener { deleteTodo(todo.key) }

        editTodoCancleImageButton.setOnClickListener {
            finish()
        }

        editTodoSaveTextButton.setOnClickListener {
            saveTodo(todo.key)
        }
    }

    private fun deleteTodo(key: String) {
        database.child("Users/${auth.currentUser!!.uid}/Schedules/Todo/${key}").removeValue()
            .addOnCompleteListener {
                finish()
            }
    }

    private fun saveTodo(key: String) {
        val myTodoReference =
            database.child("Users/${auth.currentUser!!.uid}/Schedules/Todo")

        var todo = Todo(
            key,
            auth.currentUser!!.uid,
            "DEFAULT_TODO",
            findViewById<TextView>(R.id.editTodoContentText).text.toString(),
            System.currentTimeMillis(),
            false
        )
        if (editTodoScheduleCheckBox.isChecked) {
            todo.type = "SCHEDULED_TODO"
            val time_text = "${if (currentHour!! < 10) {
                "0${currentHour}"
            } else {
                currentHour
            }}:${if (currentMinute!! < 10) {
                "0${currentMinute}"
            } else {
                currentMinute
            }}:00"
            todo.scheduledDateTime =
                Timestamp.valueOf(
                    "${currentYear!! + 1900}-${currentMonth!! + 1}-${currentDayOfMonth} ${time_text}"
                ).time
        }

        val updates = HashMap<String, Any?>()
        updates["$key"] = todo

        myTodoReference.updateChildren(updates)

        finish()
    }
}
