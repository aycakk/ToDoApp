package com.example.todo.data.Repo

import android.util.Log
import com.example.todo.data.datasource.TaskDataSource

class TasksRepository(var tds:TaskDataSource) {

    suspend fun save(task_title:String,task_explain:String,task_startdate:Long,task_end_date:Long,date:Long){
        tds.save(task_title, task_explain, task_startdate,task_end_date, date )
        Log.d("Saverepo", "viewsave ")
    }
   suspend fun loading()=tds.loading()
    suspend fun delete(task_id:Int)=tds.delete(task_id)
    suspend fun update( task_id: Int,task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
                date: Long)=tds.update( task_id,task_title, task_explain, task_startdate, task_end_date,date)

    suspend fun isChecked(task_id: Int, isCheck: Boolean)=tds.isChecked(task_id,isCheck)


}