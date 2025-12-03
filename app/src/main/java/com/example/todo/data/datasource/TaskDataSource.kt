package com.example.todo.data.datasource


import android.util.Log
import com.example.todo.data.entity.Tasks
import com.example.todo.data.room.TaskDao
import com.example.todo.sync.FirestoreSyncRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskDataSource(private val taskDao: TaskDao,private val firestoreSyncRepository: FirestoreSyncRepository)
{

  // Yeni görev kaydet
  suspend fun save(
    taskTitle: String,
    taskExplain: String,
    taskStartDate: Long,
    taskEndDate: Long,
    date: Long
  ) {
    try {
      val newTask = Tasks(

        userId = 0,
        title = taskTitle,
        explain = taskExplain,
        isCompleted = false,
        startDate = taskStartDate,
        endDate = taskEndDate,
        date = date,
        version = 1, // default versiyon
        createdTime = System.currentTimeMillis()
      )
      taskDao.save(newTask)
      Log.d("TASK_ADD", "Yeni görev ID: ${newTask.id}")

    } catch (e: Exception) {
      Log.e("TaskDataSource", "Save error", e)
    }
  }

  // Tüm görevleri yükle
  suspend fun loading(): List<Tasks> =
    withContext(Dispatchers.IO) {
      return@withContext taskDao.loadingtask()
    }

  // Görev sil
  suspend fun delete(taskId: String) {
    try {
      val deleteTask = Tasks(
        id = taskId,
        userId = 0,
        title = "",
        explain = "",
        isCompleted = false,
        startDate = 0,
        endDate = 0,
        date = 0,
        version = 1,
        createdTime = System.currentTimeMillis()
      )
      taskDao.delete(deleteTask)
    } catch (e: Exception) {
      Log.e("TaskDataSource", "Delete error", e)
    }
  }

  // Görev güncelle
  suspend fun update(
    taskId: String,
    taskTitle: String,
    taskExplain: String,
    taskStartDate: Long,
    taskEndDate: Long,
    date: Long
  ) {
    val updateTask = Tasks(
      id = taskId,
userId = 0,
      title = taskTitle,
      explain = taskExplain,
      isCompleted = false,
      startDate = taskStartDate,
      endDate = taskEndDate,
      date = date,
      version = 1,
      createdTime = System.currentTimeMillis()
    )
    taskDao.update(updateTask)
  }

  // Tamamlandı mı? güncelle
  suspend fun isChecked(taskId: String, isCheck: Boolean) {
    taskDao.updateChecked(taskId, isCheck)
  }

  suspend fun syncAll() {
   firestoreSyncRepository.syncAll()
  }


}