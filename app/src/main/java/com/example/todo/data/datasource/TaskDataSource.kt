package com.example.todo.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.entity.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class TaskDataSource(var collectionTasks:CollectionReference) {
  var TaskList = MutableLiveData<List<Tasks>>()



  fun save(
    task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
    date: Long
  ) {
    try {
      val currentUser = FirebaseAuth.getInstance().currentUser
      if (currentUser != null) {
        val docRef = collectionTasks.document()
        val userId = currentUser.uid

        val newtask =
          Tasks("", userId,task_title, task_explain, false, "", task_startdate, task_end_date, date, 0)

        collectionTasks.document().set(newtask)
        Log.d("SaveTask", "Görev başarıyla kaydedildi.${docRef.id}")

      } else {
        Log.e("SaveTask", "Kullanıcı oturumu açık değil!")
      }
    }
    catch (e:Exception){
      Log.d("", "",e)

    }    }






  fun loading(): MutableLiveData<List<Tasks>> {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
      val userId = currentUser.uid
      Log.d("user", "userıd: $userId")
      collectionTasks.whereEqualTo("userID", userId)
      .addSnapshotListener { value, error ->
        if (error != null) {
          Log.e("FirestoreError", "Error getting tasks", error)
          return@addSnapshotListener
        }
      if (value != null) {
        val list = ArrayList<Tasks>()
        for (d in value.documents) {
          val task = d.toObject(Tasks::class.java)
          if (task != null) {
            task.id = d.id
            Log.d("FirestoreData", "Document ID: ${d.id}, Data: ${d.data}")
            list.add(task)
          }

        }

        TaskList.value = list

      }
    }

  }

    return TaskList
  }

  fun delete(task_id:String){
    collectionTasks.document(task_id).delete()

  }
  fun update( task_id: String,task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
              date: Long){
    val updatetask=HashMap<String,Any>()
    updatetask["title"]=task_title
    updatetask["explain"]=task_explain
    updatetask["startdate"]=task_startdate
    updatetask["end_date"]=task_end_date
    updatetask["date"]=date
    collectionTasks.document(task_id).update(updatetask)


  }
}

