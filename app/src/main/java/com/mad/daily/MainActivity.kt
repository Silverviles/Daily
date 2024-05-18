package com.mad.daily

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad.daily.adapter.RecyclerHelper
import com.mad.daily.adapter.TaskAdapter
import com.mad.daily.database.AppDatabase
import com.mad.daily.database.Task
import com.mad.daily.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val new: Int = 1
    private val edit: Int = 2
    private lateinit var binding: ActivityMainBinding
    private lateinit var items: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var model: AppDatabase
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = AppDatabase.getInstance(applicationContext)

        binding.floatingInsert.setOnClickListener { addTask() }

        recyclerView = findViewById(R.id.taskItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        items = mutableListOf()
        adapter = TaskAdapter(items, R.layout.list_item, this, model)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(RecyclerHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fetchTasksAsync()
    }

    private fun addTask() {
        val intent = Intent(this, AddTask::class.java)
        startActivityForResult(intent, new)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task?
            if (requestCode == new) {
                task?.let {
                    items.add(it)
                    mainScope.launch(Dispatchers.IO) {
                        model.taskDao().addTask(it)
                        withContext(Dispatchers.Main) {
                            adapter.notifyItemInserted(items.size - 1)
                        }
                    }
                }
            } else if (requestCode == edit) {
                val position = data.getIntExtra("index", -1)
                task?.let {
                    items[position] = it
                    mainScope.launch(Dispatchers.IO) {
                        model.taskDao().updateTask(it)
                        withContext(Dispatchers.Main) {
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    private fun fetchTasksAsync() {
        mainScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                model.taskDao().getAllTasks()
            }
            updateUI(tasks)
        }
    }

    private fun updateUI(tasks: List<Task>) {
        items.clear()
        items.addAll(tasks)
        adapter.notifyDataSetChanged()
    }
}
