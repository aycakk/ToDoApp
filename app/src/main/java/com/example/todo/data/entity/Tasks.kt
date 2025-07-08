package com.example.todo.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.Date
@Entity(tableName = "task")
data class Tasks(@PrimaryKey(autoGenerate = true)
                 @ColumnInfo(name = "task_id") @NotNull var id:Int,
                 @ColumnInfo(name = "user_id") @NotNull var userID:Int,
                 @ColumnInfo(name = "title") @NotNull var title: String = "",                // Başlık
                 @ColumnInfo(name = "explain") @NotNull var explain:String = "",          // Açıklama
                 @ColumnInfo(name = "iscompleate") var isCompleted: Boolean = false,      // Tamamlandı mı?
                 @ColumnInfo(name = "start_time") @NotNull var startdate: Long = 0L,
                 @ColumnInfo(name = "end_time") @NotNull var end_date: Long = 0L,// Bitiş tarihi
                 @ColumnInfo(name = "date") @NotNull var date:  Long = 0L,
                 @ColumnInfo(name = "created_timr") @NotNull var createdAt: Long = System.currentTimeMillis()):Serializable{}
