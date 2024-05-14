package com.mad.daily.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") var taskId: Int = 0,
    @ColumnInfo(name = "task_desc") var taskDesc: String,
    @ColumnInfo(name = "priority") var priority: Int,
    @ColumnInfo(name = "completed") var completed: Boolean
) : Serializable {
    constructor() : this(0, "", 0, false)
}