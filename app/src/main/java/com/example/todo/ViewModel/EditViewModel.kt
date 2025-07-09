package com.example.todo.ViewModel

import androidx.lifecycle.ViewModel
import com.example.todo.data.Repo.TasksRepostory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(var trs: TasksRepostory): ViewModel(){
    fun delete(task_id:Int)=CoroutineScope(Dispatchers.Main).launch { trs.delete(task_id) }

    fun update( task_id: Int,task_title: String, task_explain: String, task_startdate: Long, task_end_date: Long,
                date: Long)=CoroutineScope(Dispatchers.Main).launch {trs.update(task_id,task_title,task_explain,task_startdate,task_end_date,date)}
}