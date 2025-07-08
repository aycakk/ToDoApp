package com.example.todo.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.data.entity.Tasks

interface TaskDao {
 @Insert
 suspend fun save(task: Tasks)

 @Query("SELECT FROM * tasks")
 suspend fun loadingtask():List<Tasks>

 @Delete
 suspend fun delete(task: Tasks)
}