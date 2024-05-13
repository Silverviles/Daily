package com.mad.daily.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") val taskId: Int = 0,
    @ColumnInfo(name = "task_desc") val taskDesc: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "completed") val completed: Boolean
) : Serializable