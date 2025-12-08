package com.example.todo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todo.data.room.DataBase
import com.example.todo.sync.FirestoreSyncRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val syncRepository: FirestoreSyncRepository

    init {
        val db = DataBase.getInstance(context)
        val taskDao = db.gettaskdao()
        syncRepository = FirestoreSyncRepository(taskDao)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            syncRepository.syncAll()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
