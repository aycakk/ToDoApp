package com.example.todo.utils

import android.content.Context
import com.example.todo.data.entity.SuggestedTask
import com.example.todo.data.entity.Tasks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



object JsonHelper {
    fun loadSuggestions(context: Context): List<SuggestedTask> {
        val json = context.assets.open("suggested_tasks.json")
            .bufferedReader().use { it.readText() }

        val type = object : TypeToken<List<SuggestedTask>>() {}.type
        return Gson().fromJson(json, type)
    }
}
