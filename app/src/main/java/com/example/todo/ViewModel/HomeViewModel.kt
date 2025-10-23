package com.example.todo.ViewModel

import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.todo.data.entity.Tasks
import com.example.todo.data.Repo.TasksRepostory
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.Calendar


import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(var trs: TasksRepostory): ViewModel() {
    var tasklist=MutableLiveData<List<Tasks>>()


    init {
        loading()
        loadTasksForDate(Calendar.getInstance())
    }
    fun loading(){CoroutineScope(Dispatchers.Main).launch {
        tasklist.value=trs.loading() }

    }
    fun updateTaskCompletion(taskId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val task = trs.getTaskById(taskId)
            task?.let {

                trs.ischecked(taskId,isChecked)
               loading() // listeyi güncellemek için
            }
        }
    }
    private val _weeklyTasks = MutableLiveData<List<Pair<DayOfWeek, List<Tasks>>>>()
    val weeklyTasks: LiveData<List<Pair<DayOfWeek, List<Tasks>>>> = _weeklyTasks

    fun loadWeeklyTasks() {
        viewModelScope.launch {
            val (startOfWeek, endOfWeek) = getStartAndEndOfCurrentWeek()
            val tasks = trs.getTasksForWeek(startOfWeek, endOfWeek)

            val grouped = tasks.groupBy {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                val day = cal.get(Calendar.DAY_OF_WEEK)
                DayOfWeek.of(if (day == 1) 7 else day - 1) // Pazartesi:1, Pazar:7
            }.toSortedMap()

            _weeklyTasks.value = grouped.map { it.key to it.value }
        }
    }

    private fun getStartAndEndOfCurrentWeek(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeek = cal.timeInMillis

        cal.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = cal.timeInMillis

        return startOfWeek to endOfWeek
    }

    fun loadTasksForDate(date: Calendar) {
        val start = getStartOfDay(date)
        val end = getEndOfDay(date)

        viewModelScope.launch {
            val result = trs.getTasksForWeek(
                start.timeInMillis,
                end.timeInMillis
            )
            tasklist.value = result
        }
    }
    private fun getStartOfDay(date: Calendar): Calendar {
        return (date.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun getEndOfDay(date: Calendar): Calendar {
        return (date.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
    }
    fun getCurrentWeekDays(): List<Calendar> {
        val days = mutableListOf<Calendar>()
        val cal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        repeat(7) {
            days.add(cal.clone() as Calendar)
            cal.add(Calendar.DAY_OF_WEEK, 1)
        }
        return days
    }





}





