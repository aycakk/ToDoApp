package com.example.todo.data.entity


import java.io.Serializable
import java.util.Date

data class Tasks(var id: String = "",
                 var userID:String="",// Firestore Belge ID'si
                 var title: String = "",                // Başlık
                 var explain:String = "",          // Açıklama
                 var isCompleted: Boolean = false,      // Tamamlandı mı?
                 var category: String = "",             // Kategori
                 var startdate: Long = 0L,
                 var end_date: Long = 0L,// Bitiş tarihi
                 var date:  Long = 0L,
                 var createdAt: Long = System.currentTimeMillis()):Serializable{}
