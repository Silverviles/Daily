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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val new: Int = 1
    private val edit: Int = 2
    private lateinit var binding: ActivityMainBinding
    private lateinit var items: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var model: AppDatabase
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        model = AppDatabase.getInstance(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        binding.floatingInsert.setOnClickListener { addTask() }
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
                items.add(task!!)
                model.taskDao().addTask(task)
                adapter.notifyItemInserted(items.size - 1)
            } else if (requestCode == edit) {
                val position = data.getIntExtra("index", -1)
                items[position] = task!!
                model.taskDao().updateTask(task)
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun fetchTasksAsync() {
        coroutineScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                model.taskDao().getAllTasks()
            }
            updateUI(tasks)

            recyclerView = findViewById(R.id.taskItems)
            adapter = TaskAdapter(items, R.layout.list_item, MainActivity(), model)

            val itemTouchHelper = ItemTouchHelper(RecyclerHelper(adapter))
            itemTouchHelper.attachToRecyclerView(recyclerView)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(MainActivity())
        }
    }

    private fun updateUI(tasks: List<Task>) {
        adapter.tasks = tasks.toMutableList()
    }
}