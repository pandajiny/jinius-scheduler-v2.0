package com.example.jiniusscheduler.schedules

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.database.Database
import com.example.jiniusscheduler.utils.ScheduleUtils
import kotlinx.android.synthetic.main.fragment_schedule_item_todo_simple.view.*
import kotlinx.android.synthetic.main.fragment_schedule_item_todo_scheduled.view.*
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class ScheduleListAdapter(
    private var data: ArrayList<Database.SCHEDULE>
) :
    RecyclerView.Adapter<ScheduleListAdapter.BaseViewHolder>() {
    //    constants for data type
    companion object {
        private const val TYPE_DEFAULT_TODO = 1000
        private const val TYPE_SCHEDULED_TODO = 1001
    }


    //    base view holder abstract class
    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Any)
    }

    inner class DefaultTodoViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Any) {
            if (item is Database.SCHEDULE) {
//                set the content text
                itemView.defaultTodoItemContentText.text = item.title
//               check Todo object's done
                itemView.defaultTodoItemContentText.isEnabled = !item.done
                itemView.defaultTodoItemCheckBox.isChecked = item.done

                if (itemView.defaultTodoItemCheckBox.isChecked) {
                    itemView.defaultTodoItemContentText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    Log.w("else", item.key)
                    itemView.defaultTodoItemContentText.paintFlags = 0
                }

                itemView.defaultTodoItemContentText.setOnClickListener {
//                    activity.startEditTodoActivity(item.key)
                }

                itemView.defaultTodoItemCheckBox.setOnClickListener {
                    ScheduleUtils().toggleDone(id = item.key, prevValue = item.done)


                    if (itemView.defaultTodoItemCheckBox.isChecked) {
                        itemView.defaultTodoItemContentText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        Log.w("else", item.key)
                        itemView.defaultTodoItemContentText.paintFlags = 0
                    }
                }
            }
        }
    }

    inner class ScheduledTodoViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Any) {
            if (item is Database.SCHEDULE) {
//                set the content text
                itemView.scheduledTodoItemContentText.text = item.title
//               check Todo object's done
                itemView.scheduledTodoItemCheckBox.isChecked = item.done

                if (itemView.scheduledTodoItemCheckBox.isChecked) {
                    itemView.scheduledTodoItemContentText.paintFlags =
                        Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    itemView.scheduledTodoItemContentText.paintFlags = 0
                }

                itemView.scheduledTodoItemRemainTimeText.text =
                    getRemainTime(item.scheduledDateTime)

                itemView.scheduledTodoItemContentText.setOnClickListener {
//                    activity.startEditTodoActivity(item.key)
                }

                itemView.scheduledTodoItemCheckBox.setOnClickListener {
                    ScheduleUtils().toggleDone(id = item.key, prevValue = item.done)

                    if (itemView.scheduledTodoItemCheckBox.isChecked) {
                        itemView.scheduledTodoItemContentText.paintFlags =
                            Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        itemView.scheduledTodoItemContentText.paintFlags = 0
                    }
                }
            }
        }
    }
//    end of view holder


    //    function for classifying data type
    override fun getItemViewType(position: Int): Int {
        return if (data[position].scheduledDateTime == null) {
            TYPE_DEFAULT_TODO
        } else {
            TYPE_SCHEDULED_TODO
        }
    }

    //    when data come, create view holder with view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        check the view type
        return when (viewType) {
            TYPE_DEFAULT_TODO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_schedule_item_todo_simple, parent, false)
                DefaultTodoViewHolder(view)
            }
            TYPE_SCHEDULED_TODO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_schedule_item_todo_scheduled, parent, false)
                ScheduledTodoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val element = data[position]
        when (holder) {
            is DefaultTodoViewHolder -> holder.bind(element as Database.SCHEDULE)
            is ScheduledTodoViewHolder -> holder.bind(element as Database.SCHEDULE)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    private fun getRemainTime(timestamp: Long?): String {
        if (timestamp != null) {
            val currentTime = Date().time
            var isPassed = false
            var remainTime = timestamp - currentTime
            if (remainTime < 0) {
                isPassed = true
                remainTime *= -1
            }
            var hour = remainTime / 1000 / 3600
            var day: Long? = null
            if (hour > 24) {
                day = hour / 24
                hour %= 24
            }
            val minutes = remainTime / 1000 / 60 % 60
            val seconds = remainTime / 1000 % 60
            if (day == null) {
                return "${if (isPassed) {
                    "-"
                } else {
                    ""
                }
                } ${hour}:${minutes}:${seconds}"
            } else {
                return "${if (isPassed) {
                    "-"
                } else {
                    ""
                }} ${day}days ${hour}:${minutes}:${seconds}"
            }
        }
        return ""
    }
}