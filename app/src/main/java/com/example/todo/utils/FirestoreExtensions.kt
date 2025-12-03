package com.example.todo.utils




import com.example.todo.data.entity.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

fun Tasks.toFirestoreMap() = mapOf(
    "id" to id,                // 🔄 "task_id" yerine "id"
    "user_id" to userId,
    "title" to title,
    "explain" to explain,
    "is_complete" to isCompleted,
    "start_time" to startDate,
    "end_time" to endDate,
    "date" to date,
    "version" to version,
    "created_time" to createdTime,
    "updated_time" to updatedTime
)

fun DocumentSnapshot.toTask(): Tasks? {
    val data = this.data ?: return null
    return Tasks(
        id = data["id"] as? String ?: UUID.randomUUID().toString(),
        userId = (data["user_id"] as? Number)?.toLong() ?: 0L,
        title = data["title"] as? String ?: "",
        explain = data["explain"] as? String ?: "",
        isCompleted = data["is_complete"] as? Boolean ?: false,
        startDate = (data["start_time"] as? Number)?.toLong() ?: 0L,
        endDate = (data["end_time"] as? Number)?.toLong() ?: 0L,
        date = (data["date"] as? Number)?.toLong() ?: 0L,
        version = (data["version"] as? Number)?.toInt() ?: 1,
        createdTime = (data["created_time"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        updatedTime = (data["updated_time"] as? Number)?.toLong() ?: System.currentTimeMillis()
    )
}