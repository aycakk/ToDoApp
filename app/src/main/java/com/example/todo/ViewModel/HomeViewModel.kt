package com.example.todo.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.data.entity.Tasks
import com.example.todo.data.Repo.TasksRepostory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(var trs: TasksRepostory): ViewModel() {
    var tasklist=MutableLiveData<List<Tasks>>()
    init {
        loading()
    }
    fun loading(){
        tasklist=trs.loading()
    }

    fun delete(task_id:String)=trs.delete(task_id)
}