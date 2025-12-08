package com.example.todo.data.datasource


import android.util.Log
import com.example.todo.data.entity.Tasks
import com.example.todo.data.entity.markUpdated
import com.example.todo.data.room.TaskDao
import com.example.todo.sync.FirestoreSyncRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskDataSource(
  private val taskDao: TaskDao,
  private val firestoreSyncRepository: FirestoreSyncRepository
) {

  // ⭐ Yeni görev ekle
  suspend fun save(
    taskTitle: String,
    taskExplain: String,
    taskStartDate: Long,
    taskEndDate: Long,
    date: Long
  ) {
    val newTask = Tasks(
      userId = 0,
      title = taskTitle,
      explain = taskExplain,
      isCompleted = false,
      isDeleted = false,
      startDate = taskStartDate,
      endDate = taskEndDate,
      date = date,
      version = 1,
      createdTime = System.currentTimeMillis(),
      updatedTime = System.currentTimeMillis()
    )
    taskDao.save(newTask)
  }

  // ⭐ Aktif görevleri yükle
  suspend fun loading(): List<Tasks> = taskDao.loadActiveTasks()

  // ⭐ Güncelleme
  suspend fun update(
    taskId: String,
    taskTitle: String,
    taskExplain: String,
    taskStartDate: Long,
    taskEndDate: Long,
    date: Long
  ) {
    val oldTask = taskDao.getTaskById(taskId)

    val updated = oldTask.copy(
      title = taskTitle,
      explain = taskExplain,
      startDate = taskStartDate,
      endDate = taskEndDate,
      date = date
    )

    updated.markUpdated() // version++ + updatedTime
    taskDao.update(updated)
  }

  // ⭐ Soft delete
  suspend fun delete(taskId: String) {
    val oldTask = taskDao.getTaskById(taskId)

    val updated = oldTask.copy(
      isDeleted = true
    )

    updated.markUpdated()
    taskDao.update(updated) // Artık soft delete oldu
  }

  // ⭐ Checkbox (tamamlandı mı)
  suspend fun isChecked(taskId: String, isCheck: Boolean) {
    val oldTask = taskDao.getTaskById(taskId)

    val updated = oldTask.copy(
      isCompleted = isCheck
    )

    updated.markUpdated()
    taskDao.update(updated)
  }

  suspend fun syncAll() {
    firestoreSyncRepository.syncAll()
  }
}
