package com.example.todo.di

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication:Application() {
}