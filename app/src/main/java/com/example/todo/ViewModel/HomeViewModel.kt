package com.example.todo.ViewModel

import android.util.Log
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
    val progress = MutableLiveData<Int>()
    init {
        loading()
    }
    fun loading(){CoroutineScope(Dispatchers.Main).launch {
        tasklist.value=trs.loading()
        val list = trs.loading()
        tasklist.value = list
        calculateProgress(list)
    }

    }
    fun isChecked(task_id: Int, isCheck: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            trs.isChecked(task_id, isCheck)

            // Veriyi doğrudan al
            val list = trs.loading()   // Güncel listeyi direkt repository'den çekiyoruz
            tasklist.value = list

            calculateProgress(list)    //Boş gelmez, güncel listeyle hesaplar
        }
    }

    fun calculateProgress(list: List<Tasks>) {
        val total = list.size
        val completed = list.count { it.isCompleted }
        val percent = if (total > 0) (completed * 100) / total else 0
        progress.postValue(percent)
        Log.d("PROGRESS_DEBUG", "total=$total, completed=$completed, percent=$percent")
    }


}