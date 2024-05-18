package com.mad.daily

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mad.daily.database.Task

class AddTask : AppCompatActivity() {
    private lateinit var radioButtons: RadioGroup
    private var task: Task? = null
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        radioButtons = findViewById(R.id.radioButtons)
        editText = findViewById(R.id.input_description)

        val intent = intent
        task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("task", Task::class.java)
        } else {
            intent.getSerializableExtra("task") as? Task
        }

        task?.let {
            editText.setText(it.taskDesc)
        }
    }

    fun clearTask(view: View) {
        setResult(RESULT_CANCELED)
        finish()
    }

    fun clicked(view: View) {
        if (task == null) {
            task = Task()
        }

        task!!.taskDesc = editText.text.toString()
        task!!.completed = false
        when (radioButtons.checkedRadioButtonId) {
            R.id.important -> task!!.priority = 1
            R.id.average -> task!!.priority = 2
            R.id.unimportant -> task!!.priority = 3
        }
        val resultIntent = Intent().apply {
            putExtra("task", task)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
