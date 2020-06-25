package com.example.jiniusscheduler.schedules.todo

import TimeCallbackListener
import TimePickerFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.database.SaveDataCallBack
import com.example.jiniusscheduler.database.Database
import com.example.jiniusscheduler.schedules.pickers.DateCallbackListener
import com.example.jiniusscheduler.schedules.pickers.DatePickerFragment
import com.example.jiniusscheduler.utils.ScheduleUtils
import com.example.jiniusscheduler.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_todo_edit.*
import org.jetbrains.anko.toast

class EditTodoActivity : AppCompatActivity() {

    private var todo = Database.SCHEDULE()
    private var dateTime = TimeUtils().createCurrentDateTime()
    private var isLoading = false

    enum class EDIT_TYPES {
        ADD_TODO,
        EDIT_TODO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_edit)
    }


    override fun onStart() {
        super.onStart()
        val key = intent.extras?.getString("key")
        if (key.isNullOrEmpty()) {
            initializeView(EDIT_TYPES.ADD_TODO, null)
        } else {
            initializeView(EDIT_TYPES.EDIT_TODO, key)
        }
    }

    private fun initializeView(type: EDIT_TYPES, key: String?) {
        editTodoCancleImageButton.setOnClickListener {
            finish()
        }

        editTodoSaveTextButton.setOnClickListener {
            saveTodo(key)
        }


        editTodoDateText.text =
            TimeUtils().dateToString(dateTime.year, dateTime.month, dateTime.dayOfMonth)
        editTodoTimeText.text =
            TimeUtils().timeToString(dateTime.hourOfDay, dateTime.minute)

        editTodoScheduledCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            editTodoDateText.isEnabled = isChecked
            editTodoTimeText.isEnabled = isChecked
        }

//        when click date text
        editTodoDateText.setOnClickListener {
//            start dialog fragment with call back
            val dateCallbackListener = object :
                DateCallbackListener {
                override fun onDataReceived(year: Int, month: Int, dayOfMonth: Int) {
                    dateTime.year = year
                    dateTime.month = month
                    dateTime.dayOfMonth = dayOfMonth
                    editTodoDateText.text = TimeUtils().dateToString(year, month, dayOfMonth)
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
                    dateTime.hourOfDay = hourOfDay
                    dateTime.minute = minute
                    editTodoTimeText.text = TimeUtils().timeToString(hourOfDay, minute)
                }
            }
            TimePickerFragment(timeCallbackListener).show(supportFragmentManager, "timePicker")
        }

        if (type == EDIT_TYPES.EDIT_TODO) {
            ScheduleUtils().getSchedule(
                key!!,
                object : Database.GetDataCallBack<Database.SCHEDULE> {
                    override fun onDataReceived(data: Database.SCHEDULE) {
                        editMode(data)
                    }

                    override fun onCanceled(message: String) {
                        toast(message)
                        finish()
                    }
                })
        }
    }

    private fun editMode(todo: Database.SCHEDULE) {

        editTodoContentText.setText(todo.title)

        if (todo.scheduledDateTime != null) {
            dateTime = TimeUtils().getDateTime(todo.scheduledDateTime!!)
            editTodoDateText.text =
                TimeUtils().dateToString(dateTime.year, dateTime.month, dateTime.dayOfMonth)
            editTodoTimeText.text =
                TimeUtils().timeToString(dateTime.hourOfDay, dateTime.minute)
            editTodoScheduledCheckBox.isChecked = true
        }

        editTodoDeleteFAB.visibility = View.VISIBLE
    }

    private fun saveTodo(key: String?) {
        if (isLoading) {
            return
        } else {
            isLoading = true
        }
        var todo = Database.SCHEDULE(
            type = if (!editTodoScheduledCheckBox.isChecked) {
                Database.SCHEDULE_TYPES.SIMPLE_TODO
            } else {
                Database.SCHEDULE_TYPES.SCHEDULED_TODO
            },
            title = findViewById<TextView>(R.id.editTodoContentText).text.toString(),
            requestTimestamp = System.currentTimeMillis(),
            done = false,
            scheduledDateTime = if (!editTodoScheduledCheckBox.isChecked) {
                null
            } else {
                TimeUtils().getTimestamp(dateTime)
            }
        )

        ScheduleUtils().saveTodo(key, todo, object : SaveDataCallBack<Database.SCHEDULE> {
            override fun onSuccess() {
                finish()
            }

            override fun onCanceled(message: String) {
                toast(message)
                isLoading = false
            }
        })
    }
}
