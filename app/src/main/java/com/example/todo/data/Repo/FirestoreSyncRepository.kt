package com.example.todo.sync

import android.util.Log
import com.example.todo.data.entity.Tasks
import com.example.todo.data.room.TaskDao
import com.example.todo.utils.toFirestoreMap
import com.example.todo.utils.toTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSyncRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncAll() {
        try {
            Log.d("SYNC", "------ Sync başladı ------")

            val localList = taskDao.loadAllTasks()
            val remoteSnap = firestore.collection("tasks").get().await()
            val remoteList = remoteSnap.documents.mapNotNull { it.toTask() }

            val localMap = localList.associateBy { it.id }
            val remoteMap = remoteList.associateBy { it.id }

            val allIds = (localMap.keys + remoteMap.keys).toSet()

            val batch = firestore.batch()

            for (id in allIds) {

                val local = localMap[id]
                val remote = remoteMap[id]

                when {

                    // ------------------------------
                    // 1) Remote var / Local yok
                    // ------------------------------
                    local == null && remote != null -> {
                        taskDao.insert(remote)
                        Log.d("SYNC_ADD_LOCAL", "Local'e eklendi → ${remote.title}")
                    }

                    // ------------------------------
                    // 2) Local var / Remote yok
                    // ------------------------------
                    local != null && remote == null -> {
                        val doc = firestore.collection("tasks").document(local.id)
                        batch.set(doc, local.toFirestoreMap(), SetOptions.merge())
                        Log.d("SYNC_ADD_REMOTE", "Remote'a eklendi → ${local.title}")
                    }

                    // ------------------------------
                    // 3) Her iki tarafta veri var
                    // ------------------------------
                    local != null && remote != null -> {

                        val localDeleted = local.isDeleted
                        val remoteDeleted = remote.isDeleted

                        val winner: Tasks = when {
                            // --------------------------------------
                            // A) SİLME (isDeleted=true) HER ZAMAN KAZANIR
                            // --------------------------------------
                            localDeleted != remoteDeleted -> {
                                if (localDeleted == true) local else remote
                            }

                            // --------------------------------------
                            // B) Version numarası büyük olan kazanır
                            // --------------------------------------
                            local.version != remote.version -> {
                                if (local.version > remote.version) local else remote
                            }

                            // --------------------------------------
                            // C) Daha yeni updatedTime kazanır
                            // --------------------------------------
                            local.updatedTime != remote.updatedTime -> {
                                if (local.updatedTime > remote.updatedTime) local else remote
                            }

                            // --------------------------------------
                            // D) Çok nadir durumda eşitse → ID tie-break
                            // --------------------------------------
                            else -> {
                                if (local.id > remote.id) local else remote
                            }
                        }

                        // WINNER LOCAL'E YAZ
                        taskDao.insert(winner)

                        // WINNER REMOTE'A YAZ
                        val doc = firestore.collection("tasks").document(winner.id)
                        batch.set(doc, winner.toFirestoreMap(), SetOptions.merge())

                        Log.d(
                            "SYNC_CONFLICT",
                            "Winner → ${winner.title}, deleted=${winner.isDeleted}, v=${winner.version}"
                        )
                    }
                }
            }

            batch.commit().await()
            Log.d("SYNC", "------ Sync tamamlandı ------")

        } catch (e: Exception) {
            Log.e("SYNC_ERROR", "Sync hatası:", e)
        }
    }


}
