package com.mad.daily.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mad.daily.AddTask
import com.mad.daily.MainActivity
import com.mad.daily.R
import com.mad.daily.database.AppDatabase
import com.mad.daily.database.Task

class TaskAdapter(
    var tasks: MutableList<Task> = mutableListOf(),
    private val layoutId: Int,
    private val mainActivity: MainActivity,
    private val database: AppDatabase
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    fun getContext(): Context {
        return mainActivity
    }

    fun removeItem(position: Int) {
        val task = tasks[position]
        tasks.remove(task)
        notifyItemRemoved(position)
        database.taskDao().deleteTask(task)
    }

    fun editItem(position: Int) {
        val task = tasks[position]
        val intent = Intent(mainActivity, AddTask::class.java)
        intent.putExtra("task", task)
        intent.putExtra("index", position)
        mainActivity.startActivity(intent)
        notifyItemChanged(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskDesc.text = task.taskDesc

        when (task.priority) {
            0 -> holder.priority.setImageResource(R.drawable.red_circle)
            1 -> holder.priority.setImageResource(R.drawable.yellow_circle)
            2 -> holder.priority.setImageResource(R.drawable.green_circle)
        }
    }


    override fun getItemCount(): Int {
        return tasks.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskDesc: TextView = itemView.findViewById(R.id.taskDesc)
        val priority: ImageView = itemView.findViewById(R.id.taskPriority)
    }
}