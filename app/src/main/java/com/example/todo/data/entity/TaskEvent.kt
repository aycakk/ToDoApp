package com.example.todo.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "task_event")
data class TaskEvent(
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val eventId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "task_id")
    val taskId: String,

    @ColumnInfo(name = "type")
    val type: String, // CREATE, UPDATE, DELETE, CHECK

    @ColumnInfo(name = "at")
    val at: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "base_version")
    val baseVersion: Int,

    @ColumnInfo(name = "new_version")
    val newVersion: Int
)
