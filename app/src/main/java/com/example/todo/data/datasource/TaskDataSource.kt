package com.example.todo.data.datasource


import android.util.Log
import com.example.todo.data.entity.TaskEvent
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
    taskDao.insertEvent(
      com.example.todo.data.entity.TaskEvent(
        taskId = newTask.id,
        type = "CREATE",
        baseVersion = 0,
        newVersion = newTask.version
      ))
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

    val oldTask = taskDao.getTaskById(taskId) ?: return  // <-- önemli
    val baseVersion = oldTask.version

    val updated = oldTask.copy(
      title = taskTitle,
      explain = taskExplain,
      startDate = taskStartDate,
      endDate = taskEndDate,
      date = date
    )

    updated.markUpdated() // version++ + updatedTime
    taskDao.update(updated)
    taskDao.insertEvent(
      com.example.todo.data.entity.TaskEvent(
        taskId = taskId,
        type = "UPDATE",
        baseVersion = baseVersion,
        newVersion = updated.version
      ))
  }

  // ⭐ Soft delete
  suspend fun delete(taskId: String) {
    val oldTask = taskDao.getTaskById(taskId) ?: return
    val baseVersion = oldTask.version

    val updated = oldTask.copy(isDeleted = true).also { it.markUpdated() }
    taskDao.update(updated)



    taskDao.update(updated) // Artık soft delete oldu
    taskDao.insertEvent(
      com.example.todo.data.entity.TaskEvent(
        taskId = taskId,
        type = "DELETE",
        baseVersion = baseVersion,
        newVersion = updated.version
      )
    )
  }

  // ⭐ Checkbox (tamamlandı mı)
  suspend fun isChecked(taskId: String, isCheck: Boolean) {
    val oldTask = taskDao.getTaskById(taskId) ?: return
    val baseVersion = oldTask.version

    val updated = oldTask.copy(isCompleted = isCheck).also { it.markUpdated() }
    taskDao.update(updated)


    taskDao.update(updated)
    taskDao.insertEvent(
      com.example.todo.data.entity.TaskEvent(
        taskId = taskId,
        type = "CHECK",
        baseVersion = baseVersion,
        newVersion = updated.version
      ))
  }

  suspend fun syncAll() {
    firestoreSyncRepository.syncAll()
  }
  suspend fun getLastEvents(limit: Int = 50): List<TaskEvent> =
    taskDao.getLastEvents(limit)

}
