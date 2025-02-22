package com.example.todo.di

import com.example.todo.data.datasource.TaskDataSource
import com.example.todo.data.Repo.TasksRepostory
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideTasksDatasource(collectionTasks:CollectionReference):TaskDataSource{
        return TaskDataSource(collectionTasks)
    }
    @Provides
    @Singleton
    fun provideTasksRepostory(tds:TaskDataSource):TasksRepostory{
        return TasksRepostory(tds)
    }

    @Provides
    @Singleton
    fun provideCollectionReferance():CollectionReference{
        return Firebase.firestore.collection("Tasks")
    }

}