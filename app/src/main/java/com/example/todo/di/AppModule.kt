package com.example.todo.di
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.data.Repo.TasksRepository
import com.example.todo.data.datasource.TaskDataSource
import com.example.todo.data.room.DataBase
import com.example.todo.data.room.TaskDao
import com.example.todo.sync.FirestoreSyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    val MIGRATION_9_10 = object : Migration(9, 10) {

            override fun migrate(database: SupportSQLiteDatabase) {
                // ✅ Yeni kolon ekleniyor
                database.execSQL("ALTER TABLE task ADD COLUMN dummy_field TEXT")
            }

        }



    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            "task-db"
        )
            .addMigrations(MIGRATION_9_10)
            .build()

    }

    @Provides
    @Singleton
    fun provideTaskDao(db: DataBase): TaskDao = db.gettaskdao()

    // FirestoreSyncRepository provide et
    @Provides
    @Singleton
    fun provideFirestoreSyncRepository(taskDao: TaskDao): FirestoreSyncRepository =
        FirestoreSyncRepository(taskDao)

    // TaskDataSource artık hem DAO hem FirestoreSyncRepository alıyor
    @Provides
    @Singleton
    fun provideTasksDatasource(
        taskDao: TaskDao,
        firestoreSyncRepository: FirestoreSyncRepository
    ): TaskDataSource = TaskDataSource(taskDao, firestoreSyncRepository)

    @Provides
    @Singleton
    fun provideTasksRepository(ds: TaskDataSource): TasksRepository =
        TasksRepository(ds)
}