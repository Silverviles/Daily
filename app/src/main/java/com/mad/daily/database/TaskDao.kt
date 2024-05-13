package com.mad.daily.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE completed = 1")
    fun getCompletedTasks(status: Boolean): List<Task>

    @Query("SELECT * FROM tasks WHERE completed = 0")
    fun getIncompleteTasks(status: Boolean): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task) //vararg task: Task -> pass multiple task objects in a single call

    @Delete
    fun deleteTask(task: Task)
}