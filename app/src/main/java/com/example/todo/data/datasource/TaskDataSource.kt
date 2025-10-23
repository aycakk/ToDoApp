package com.example.todo.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.entity.Tasks
import com.example.todo.room.TaskDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskDataSource(var taskDao: TaskDao) {

  suspend fun save(
    task_title: String,
    task_explain: String,
    task_startdate: Long,
    task_end_date: Long,
    date: Long
  ) {
    try {
      val newTask = Tasks(
        id = 0,
        userID = 0,
        title = task_title,
        explain = task_explain,
        isCompleted = false,
        startdate = task_startdate,
        end_date = task_end_date,
        date = date, // seçilen günün epochMillis
        createdAt = System.currentTimeMillis() // kaydın oluşturulma zamanı
      )
      taskDao.save(newTask)
    } catch (e: Exception) {
      Log.d("saveError", "Task kaydedilirken hata", e)
    }
  }


  suspend fun loading(): List<Tasks> =
    withContext(Dispatchers.IO){
      return@withContext taskDao.loadingtask()
    }


  suspend fun delete(task_id:Int){
    try {
      val deletetask =
        Tasks(task_id, 0,"", "", false, 0, 0,0, 0)
      taskDao.delete(deletetask)
    }

    catch (e:Exception){
      Log.d("", "",e)

    }
  }
  suspend fun update( task_id: Int,task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
              date: Long){
    val oldTask = taskDao.getTaskById(task_id) ?: return
    val updatedTask = oldTask.copy(
      title = task_title,
      explain = task_explain,
      startdate = task_startdate,
      end_date = task_end_date,
      date = date
    )

    taskDao.update(updatedTask)
  }
  suspend fun ischecked(task_id: Int,ischecked:Boolean){
    val task = taskDao.getTaskById(task_id) ?: return
    val updatedTask = task.copy(isCompleted = ischecked)
    taskDao.update(updatedTask)
  }

  suspend fun getTaskById(id: Int){
    taskDao.getTaskById(id)
  }


    suspend fun getTasksForWeek(startDate: Long, endDate: Long): List<Tasks> {
      return taskDao.getTasksBetween(startDate, endDate)
    }
  }



