package com.example.todo.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.datasource.TaskDataSource
import com.example.todo.data.Repo.TasksRepository
import com.example.todo.room.DataBase
import com.example.todo.room.TaskDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideTasksDatasource(taskDao:TaskDao):TaskDataSource{
        return TaskDataSource(taskDao)
    }
    @Provides
    @Singleton
    fun provideTasksRepostory(tds:TaskDataSource):TasksRepository{
        return TasksRepository(tds)
    }

    @Provides
    @Singleton
    fun provideCollectionReferance(@ApplicationContext context:Context) :TaskDao{
        val vt = Room.databaseBuilder(context,DataBase::class.java,"Tasks.sqlite")
            .createFromAsset("Tasks.sqlite").build()
        return vt.gettaskdao()
    }



}