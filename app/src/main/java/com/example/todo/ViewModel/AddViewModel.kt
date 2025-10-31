package com.example.todo.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todo.data.Repo.TasksRepository
import com.example.todo.utils.NotificationHelper
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
@HiltViewModel
class AddViewModel @Inject constructor( var trs:TasksRepository):ViewModel() {

    fun save(context: Context, task_title:String, task_explain:String, task_startdate:Long, task_end_date:Long, date: Long){
        CoroutineScope(Dispatchers.Main).launch {
            trs.save(task_title,task_explain,task_startdate, task_end_date,date)
            Log.d("Saveview", "viewsave ")
            NotificationHelper.scheduleNotification(
                context =context,
                title = task_title,
                message = task_explain,
                triggerTime = task_startdate
            )

        }

    }
}