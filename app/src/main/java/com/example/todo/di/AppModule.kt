package com.example.todo.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.data.Repo.TasksRepository
import com.example.todo.data.datasource.TaskDataSource
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

    // 1) Migration EKLE
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE task ADD COLUMN version INTEGER NOT NULL DEFAULT 1"
            )
        }
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DataBase =
        Room.databaseBuilder(
            context,
            DataBase::class.java,
            "task_db"
        )
            .addMigrations(MIGRATION_1_2)
            // BURAYA EKLENİYOR
            .build()


    @Provides
    @Singleton
    fun provideTaskDao(db: DataBase): TaskDao =
        db.gettaskdao()


    @Provides
    @Singleton
    fun provideTasksDatasource(taskDao: TaskDao): TaskDataSource =
        TaskDataSource(taskDao)


    @Provides
    @Singleton
    fun provideTasksRepository(ds: TaskDataSource): TasksRepository =
        TasksRepository(ds)
}
