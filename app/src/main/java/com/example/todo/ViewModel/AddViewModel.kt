package com.example.todo.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todo.data.Repo.TasksRepostory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class AddViewModel @Inject constructor(var trs:TasksRepostory):ViewModel() {
    fun save(task_title:String,task_explain:String,task_startdate:Long,task_end_date:Long){
        trs.save(task_title,task_explain,task_startdate, task_end_date,)
        Log.d("Saveview", "viewsave ")
    }
}