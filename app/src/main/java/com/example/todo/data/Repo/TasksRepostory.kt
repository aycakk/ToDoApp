package com.example.todo.data.Repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.datasource.TaskDataSource
import com.example.todo.data.entity.Tasks

class TasksRepostory(var tds:TaskDataSource) {

    fun save(task_title:String,task_explain:String,task_startdate:Long,task_end_date:Long){
        tds.save(task_title, task_explain, task_startdate,task_end_date)
        Log.d("Saverepo", "viewsave ")
    }
    fun loading(): MutableLiveData<List<Tasks>> =tds.loading()
    fun delete(task_id:String)=tds.delete(task_id)
}