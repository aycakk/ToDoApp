package com.example.todo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.entity.Tasks

@Database(entities =[Tasks::class], version = 10)
abstract class DataBase :RoomDatabase(){
    abstract fun gettaskdao():TaskDao
    companion object {
        @Volatile private var INSTANCE: DataBase? = null

        fun getInstance(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "task_db"
                ).build()
                INSTANCE = inst
                inst
            }
        }
    }

}

