package com.example.jiniusscheduler.Schedules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.jiniusscheduler.R
import java.lang.IllegalArgumentException

class SchedulesListAdapter(private val context: Context, private val data: ArrayList<Any>) :
    RecyclerView.Adapter<SchedulesListAdapter.BaseViewHolder>() {
    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Any)
    }

    inner class SimpleTodoViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Any) {
            if (item is SimpleTodo) {
                itemView.findViewById<TextView>(R.id.simpleTodoItemContentText).text = item.content
            }
        }
    }

    //    constants for data type
    companion object {
        private const val TYPE_SIMPLE_TODO = 0
    }

    //    function for classifying data type
    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is SimpleTodo -> TYPE_SIMPLE_TODO
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    //    when data come, create view holder with view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        check the view type
        return when (viewType) {
            TYPE_SIMPLE_TODO -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.fragment_simple_todo_item, parent, false)
                SimpleTodoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val element = data[position]
        when (holder) {
            is SimpleTodoViewHolder -> holder.bind(element as SimpleTodo)
        }
    }

}