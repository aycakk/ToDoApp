package com.example.todo.utils

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todo.ReminderWorker
import java.util.concurrent.TimeUnit

object NotificationHelper {

    fun scheduleNotification(context: Context, title: String, message: String, triggerTime: Long) {

        // 🔹 Şu anki zaman ile startDate farkını hesapla
        val delay = triggerTime - System.currentTimeMillis()
        Log.d("WorkManager", "triggerTime=$triggerTime, now=${System.currentTimeMillis()}, delay=$delay")

        // 🔹 Geçmiş tarihse planlama yapma
        if (delay <= 0) {
            Log.w("WorkManager", "Geçmiş zamana bildirim planlanmadı.")
            return
        }

        // 🔹 WorkManager'a gidecek veriyi hazırla
        val data = workDataOf(
            "title" to title,
            "text" to message   // 🔥 Worker’da 'text' olarak okunuyor
        )

        // 🔹 WorkManager işini oluştur
        val work = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS) // 🔥 Bildirim bu kadar gecikmeli çalışacak
            .build()

        // 🔹 İşlem WorkManager’a gönderilir
        WorkManager.getInstance(context).enqueue(work)
        Log.d("WorkManager", "Bildirim ${delay / 1000} saniye sonra tetiklenecek.")
    }
}
