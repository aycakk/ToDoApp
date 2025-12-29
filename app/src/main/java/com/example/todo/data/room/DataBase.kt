package com.example.todo.data.room


import androidx.room.Database

import androidx.room.RoomDatabase
import com.example.todo.data.entity.TaskEvent
import com.example.todo.data.entity.Tasks


@Database(
    entities = [Tasks::class, TaskEvent::class],
    version = 11,
    exportSchema = true
)
abstract class DataBase : RoomDatabase() {
    abstract fun gettaskdao(): TaskDao
}

