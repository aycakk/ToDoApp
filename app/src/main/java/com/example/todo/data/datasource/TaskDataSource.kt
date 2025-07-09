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

  suspend fun  save(
    task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
    date: Long
  ) {
    try {
        val newtask =
          Tasks(0, 0,task_title, task_explain, false, task_startdate, task_end_date, date, 0)
      taskDao.save(newtask)
      }

    catch (e:Exception){
      Log.d("", "",e)

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
    val updatetask=Tasks(task_id,0,task_title,task_explain,false,task_startdate,task_end_date,date)

   taskDao.update(updatetask)


  }
}

