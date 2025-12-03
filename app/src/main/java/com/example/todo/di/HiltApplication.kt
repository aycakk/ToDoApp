package com.example.todo.di

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

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

    companion object {
        lateinit var instance: HiltApplication
            private set
    }
}