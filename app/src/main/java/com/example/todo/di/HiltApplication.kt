package com.example.todo.di

import android.app.Application
import android.util.Log

import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todo.worker.SyncWorker
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class HiltApplication:Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this

        val firestore = FirebaseFirestore.getInstance()

        firestore.firestoreSettings =
            FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)   // CACHE KAPALI
                .build()
        val options = FirebaseApp.getInstance().options

        Log.d("FIREBASE_INFO", "projectId=${options.projectId}")
        Log.d("FIREBASE_INFO", "applicationId=${options.applicationId}")
        Log.d("FIREBASE_INFO", "apiKey=${options.apiKey}")
        Log.d("FIREBASE_INFO", "databaseUrl=${options.databaseUrl}")
        Log.d("FIREBASE_INFO", "storageBucket=${options.storageBucket}")



    }
    private fun startAutoSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodic = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "auto_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodic
        )
    }




    companion object {
        lateinit var instance: HiltApplication
            private set
    }
}