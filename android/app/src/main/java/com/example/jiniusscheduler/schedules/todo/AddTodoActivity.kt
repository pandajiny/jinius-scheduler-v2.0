package com.example.jiniusscheduler.schedules.todo

import TimeCallbackListener
import TimePickerFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.database.SaveDataCallBack
import com.example.jiniusscheduler.schedules.pickers.DateCallbackListener
import com.example.jiniusscheduler.schedules.pickers.DatePickerFragment
import com.example.jiniusscheduler.utils.ScheduleUtils
import com.example.jiniusscheduler.utils.TimeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.fragment_todo_item_scheduled.*
import org.jetbrains.anko.toast
import java.sql.Timestamp
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    private var dateTime = TimeUtils().createCurrentDateTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        addTodoDateText.text =
            TimeUtils().dateToString(dateTime.year, dateTime.month, dateTime.dayOfMonth)
        addTodoTimeText.text =
            TimeUtils().timeToString(dateTime.hourOfDay, dateTime.minute)

        initView()
    }

    private fun initView() {
//        addTodoDateText.text = "${currentYear!! + 1900}/${currentMonth!! + 1}/${currentDayOfMonth}"

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
                    dateTime.year = year
                    dateTime.month = month
                    dateTime.dayOfMonth = dayOfMonth
                    addTodoDateText.text = TimeUtils().dateToString(year, month, dayOfMonth)
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
                    dateTime.hourOfDay = hourOfDay
                    dateTime.minute = minute
                    addTodoTimeText.text = TimeUtils().timeToString(hourOfDay, minute)
                }
            }
            TimePickerFragment(timeCallbackListener).show(supportFragmentManager, "timePicker")
        }
    }

    private fun addTodo() {
        var todo = Todo(
            type = if (!addTodoScheduleCheckBox.isChecked) {
                "DEFAULT_TODO"
            } else {
                "SCHEDULED_TODO"
            },
            content = findViewById<TextView>(R.id.addTodoContentText).text.toString(),
            requestTimestamp = System.currentTimeMillis(),
            done = false,
            scheduledDateTime = if (!addTodoScheduleCheckBox.isChecked) {
                null
            } else {
                TimeUtils().getTimestamp(dateTime)
            }
        )

        ScheduleUtils().addTodo(todo, object : SaveDataCallBack<Todo> {
            override fun onSuccess() {
                finish()
            }

            override fun onCanceled(message: String) {
                toast(message)
                finish()
            }
        })
    }
}
