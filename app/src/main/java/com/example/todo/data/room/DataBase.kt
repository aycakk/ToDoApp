package com.example.todo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.entity.Tasks

@Database(entities =[Tasks::class], version = 7)
abstract class DataBase :RoomDatabase(){
    abstract fun gettaskdao():TaskDao
}