package com.example.todo.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.privacysandbox.ads.adservices.adid.AdId
import com.example.todo.data.entity.Tasks
import com.example.todo.data.Repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject





@HiltViewModel
class HomeViewModel @Inject constructor(var trs: TasksRepository , @ApplicationContext val context: Context): ViewModel() {
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
    fun isChecked(task_id: String, isCheck: Boolean) {
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
    fun syncTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            trs.sync(context)
            val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())

            val events = trs.getLastEvents(50)
            events.forEach {
                Log.d("EVENT_TRACK", "${sdf.format(java.util.Date(it.at))} ${it.type} ${it.baseVersion}->${it.newVersion} ${it.taskId}")

            }
        }
    }



}