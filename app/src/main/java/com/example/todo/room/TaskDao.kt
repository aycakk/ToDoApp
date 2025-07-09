package com.example.todo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo.data.entity.Tasks
@Dao
interface TaskDao {
 @Insert
 suspend fun save(task: Tasks)

 @Query("SELECT *FROM  task")
 suspend fun loadingtask():List<Tasks>

 @Delete
 suspend fun delete(task: Tasks)

 @Update
 suspend fun update(task: Tasks)
}