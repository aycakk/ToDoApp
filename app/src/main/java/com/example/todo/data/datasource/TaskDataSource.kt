package com.example.todo.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.entity.Tasks
import com.google.firebase.firestore.CollectionReference

class TaskDataSource(var collectionTasks:CollectionReference) {
  var TaskList = MutableLiveData<List<Tasks>>()

  fun save(task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long) {
    try {
      val newtask = Tasks("", task_title, task_explain, false, "", task_startdate, task_end_date, 0)

      collectionTasks.document().set(newtask)
      Log.d("SaveTask", "Görev başarıyla kaydedildi.")

    }
    catch (e:Exception){
     Log.e( "SaveTaskdata", "Bir hata oluştu.",e )

    }


  }

  fun loading(): MutableLiveData<List<Tasks>> {
    collectionTasks.addSnapshotListener { value, error ->
      if (value != null){
        val list=ArrayList<Tasks>()
        for(d in value.documents ){
          val task=d.toObject(Tasks::class.java)
          if (task!=null){
            task.id=d.id
            list.add(task)
          }

        }

        TaskList.value= list

    }

  }
    return TaskList
  }

  fun delete(task_id:String){
    collectionTasks.document(task_id).delete()

  }
}

