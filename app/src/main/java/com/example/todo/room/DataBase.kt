package com.example.todo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.entity.Tasks

@Database(entities =[Tasks::class], version = 1)
abstract class DataBase :RoomDatabase(){
    abstract fun gettaskdao():TaskDao
}