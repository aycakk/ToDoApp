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
    private fun isValidId(id: String?): Boolean {
        return !id.isNullOrBlank() && !id.contains("/")
    }

    suspend fun syncAll() {
        try {
            Log.d("SYNC", "Sync başladı")

            // 1) Local ve Remote verileri çek
            val localList = taskDao.loadingtask()
            Log.d("SYNC", "Local task sayısı: ${localList.size}")

            val remoteSnap = firestore.collection("tasks").get().await()
            val remoteList = remoteSnap.documents.mapNotNull { it.toTask() }
            Log.d("SYNC", "Remote task sayısı: ${remoteList.size}")

            // Boş / geçersiz id’leri filtrele
            val localMap = localList
                .filter { isValidId(it.id) }
                .associateBy { it.id }

            val remoteMap = remoteList
                .filter { isValidId(it.id) }
                .associateBy { it.id }

            val allIds = (localMap.keys + remoteMap.keys).toSet()
            Log.d("SYNC", "Toplam unique id sayısı: ${allIds.size}")

            val batch = firestore.batch()

            for (id in allIds) {
                if (!isValidId(id)) {
                    Log.e("SYNC", "Geçersiz ID tespit edildi: '$id' - atlanıyor")
                    continue
                }

                val local = localMap[id]
                val remote = remoteMap[id]

                when {
                    // 1) Sadece remote’ta var → local’e kaydet
                    local == null && remote != null -> {
                        taskDao.save(remote)
                        Log.d("SYNC", "Local'e eklendi (remote-only): ${remote.title} (${remote.id})")
                    }

                    // 2) Sadece local’de var → Firestore’a gönder
                    local != null && remote == null -> {
                        val docRef = firestore.collection("tasks").document(local.id)
                        batch.set(docRef, local.toFirestoreMap(), SetOptions.merge())
                        Log.d("SYNC", "Firebase'e eklendi (local-only): ${local.title} (${local.id})")
                    }

                    // 3) Her ikisinde de var → conflict resolution
                    local != null && remote != null -> {
                        val winner: Tasks = when {
                            remote.version > local.version -> remote
                            remote.version < local.version -> local
                            else -> if (remote.updatedTime >= local.updatedTime) remote else local
                        }

                        // Local tarafı winner'a göre güncelle
                        taskDao.update(winner)

                        // Remote tarafı winner'a göre güncelle
                        val docRef = firestore.collection("tasks").document(winner.id)
                        batch.set(docRef, winner.toFirestoreMap(), SetOptions.merge())

                        Log.d(
                            "SYNC",
                            "Conflict resolved, winner: ${winner.title} (${winner.id}) " +
                                    "[local v${local.version}, remote v${remote.version}]"
                        )
                    }

                    else -> {
                        // Hem local hem remote null ise (teoride olmaz)
                        Log.w("SYNC", "Hem local hem remote null çıktı, id: $id")
                    }
                }
            }

            batch.commit().await()
            Log.d("SYNC", "Sync tamamlandı.")
        } catch (e: Exception) {
            Log.e("SYNC", "Sync hatası", e)
        }
    }
}

