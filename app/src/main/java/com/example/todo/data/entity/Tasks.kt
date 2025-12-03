package com.example.todo.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID


@Entity(tableName = "task")
data class Tasks(
    @PrimaryKey
    @ColumnInfo(name = "task_id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") var userId: Long,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "explain") var explain: String = "",
    @ColumnInfo(name = "is_complete") var isCompleted: Boolean = false,
    @ColumnInfo(name = "start_time") var startDate: Long = 0L,
    @ColumnInfo(name = "end_time") var endDate: Long = 0L,
    @ColumnInfo(name = "date") var date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "version") var version: Int = 1,
    @ColumnInfo(name = "created_time") var createdTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_time") var updatedTime: Long = System.currentTimeMillis()
): Serializable



