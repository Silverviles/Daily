package com.mad.daily.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad.daily.MainActivity
import com.mad.daily.R
import com.mad.daily.database.AppDatabase
import com.mad.daily.database.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val layoutId: Int,
    private val mainActivity: MainActivity,
    private val database: AppDatabase
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    fun getContext(): Context {
        return mainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = layoutInflater.inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskName: TextView = itemView.findViewById(R.id.tvName)
        private val priority: ImageView = itemView.findViewById(R.id.ivUrgency)
    }
}