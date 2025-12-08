package com.example.todo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todo.data.entity.Tasks
@Dao
interface TaskDao {


  @Insert
  suspend fun save(task: Tasks)

  @Update
  suspend fun update(task: Tasks)

  @Delete
  suspend fun hardDelete(task: Tasks)  // gerekirse

  @Query("SELECT * FROM task WHERE is_deleted = 0")
  suspend fun loadActiveTasks(): List<Tasks>

  @Query("SELECT * FROM task")
  suspend fun loadAllTasks(): List<Tasks>

  @Query("SELECT * FROM task WHERE task_id = :id LIMIT 1")
  suspend fun getTaskById(id: String): Tasks

  @Query("UPDATE task SET is_complete = :isCheck WHERE task_id = :id")
  suspend fun updateChecked(id: String, isCheck: Boolean)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(task: Tasks)

}


