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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

class AddTodoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var currentYear: Int? = null
    var currentMonth: Int? = null
    var currentDayOfMonth: Int? = null

    var currentMinute: Int? = null
    var currentHour: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        auth = Firebase.auth
        database = Firebase.database.reference

        val date = Date()
        currentYear = date.year
        currentMonth = date.month
        currentDayOfMonth = date.date

        checkAuth()
        initView()
    }

    private fun initView() {
        addTodoDateText.text = "${currentYear!! + 1900}/${currentMonth!! + 1}/${currentDayOfMonth}"

//        action button option
        addTodoCancleImageButton.setOnClickListener {
            finish()
        }

        addTodoSaveTextButton.setOnClickListener {
            addTodo()
        }

//        scheduled option
        addTodoDateText.isEnabled = false
        addTodoTimeText.isEnabled = false

//         toggle check
        addTodoScheduleCheckBox.setOnClickListener {
            addTodoDateText.isEnabled = addTodoScheduleCheckBox.isChecked
            addTodoTimeText.isEnabled = addTodoScheduleCheckBox.isChecked
        }

//        when click date text
        addTodoDateText.setOnClickListener {
//            start dialog fragment with call back
            val dateCallbackListener = object :
                DateCallbackListener {
                override fun onDataReceived(year: Int, month: Int, dayOfMonth: Int) {
                    currentYear = year - 1900
                    currentMonth = month
                    currentDayOfMonth = dayOfMonth
                    addTodoDateText.text = "${year}/${month + 1}/${dayOfMonth}"
                }
            }
            DatePickerFragment(
                dateCallbackListener
            ).show(supportFragmentManager, "timePicker")
        }
//        when click time text
        addTodoTimeText.setOnClickListener {
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
                    addTodoTimeText.text = time_text
                }
            }
            TimePickerFragment(timeCallbackListener).show(supportFragmentManager, "timePicker")
        }
    }

    private fun addTodo() {
        val myTodoReference =
            database.child("Users/${auth.currentUser!!.uid}/Schedules/Todo")

        val key = myTodoReference.push().key!!

        var todo = Todo(
            key,
            auth.currentUser!!.uid,
            "DEFAULT_TODO",
            findViewById<TextView>(R.id.addTodoContentText).text.toString(),
            System.currentTimeMillis(),
            false
        )
        if (addTodoScheduleCheckBox.isChecked) {
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

    private fun checkAuth() {
        if (auth.currentUser == null) {
            finish()
        }
    }
}
