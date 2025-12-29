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

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS task_event(
                event_id TEXT NOT NULL PRIMARY KEY,
                task_id TEXT NOT NULL,
                type TEXT NOT NULL,
                at INTEGER NOT NULL,
                base_version INTEGER NOT NULL,
                new_version INTEGER NOT NULL
            )
            """.trimIndent()
            )

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
            .addMigrations(MIGRATION_10_11)
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