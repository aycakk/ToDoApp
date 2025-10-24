package com.example.todo.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.privacysandbox.ads.adservices.adid.AdId
import com.example.todo.data.entity.Tasks
import com.example.todo.data.Repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(var trs: TasksRepository): ViewModel() {
    var tasklist=MutableLiveData<List<Tasks>>()
    init {
        loading()
    }
    fun loading(){CoroutineScope(Dispatchers.Main).launch {
        tasklist.value=trs.loading() }

    }
    fun isChecked(task_id: Int, isCheck: Boolean){
        CoroutineScope(Dispatchers.Main).launch {
            trs.isChecked(task_id,isCheck)
            loading()

        }
    }



}