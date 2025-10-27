package com.example.todo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todo.Notifications.NotificationConsts.CHANNEL_ID
import com.example.todo.Notifications.NotificationConsts.CHANNEL_NAME
import kotlin.random.Random

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Görev hatırlatıcısı"
        val text  = inputData.getString("text")  ?: "Zamanı geldi 🎯"

        showNotification(title, text)
        return Result.success()
    }

    private fun showNotification(title: String, text: String) {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(ch)
        }
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()
        nm.notify(Random.nextInt(), notification)
    }
}
