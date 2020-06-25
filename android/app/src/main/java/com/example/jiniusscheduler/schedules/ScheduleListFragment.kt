package com.example.jiniusscheduler.schedules

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jiniusscheduler.R
import com.example.jiniusscheduler.database.Database
import com.example.jiniusscheduler.utils.ScheduleUtils
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import kotlinx.android.synthetic.main.fragment_schedule_list.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    val scheduleList = arrayListOf<Database.SCHEDULE>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_schedule_list, container, false)

        scheduleList.add(Database.SCHEDULE(title = "HIHI"))
//        set adapter for Schedules List RV
        val scheduleListAdapter = ScheduleListAdapter(scheduleList)
        val viewManager = LinearLayoutManager(context)

        rootView.scheduleListRV.apply {
            adapter = scheduleListAdapter
            layoutManager = viewManager
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        ScheduleUtils().getActivateSchedules(object : Database.GetDataCallBack<List<Database.SCHEDULE>> {
            override fun onDataReceived(data: List<Database.SCHEDULE>) {
                scheduleList.clear()
                data.forEach { schedule ->
                    scheduleList.add(schedule)
                }
                scheduleListRV.adapter!!.notifyDataSetChanged()
            }

            override fun onCanceled(message: String) {
                Log.e("hihi", message)
//                toast(message)
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
