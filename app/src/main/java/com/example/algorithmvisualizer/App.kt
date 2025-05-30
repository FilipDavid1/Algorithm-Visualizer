package com.example.algorithmvisualizer

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        private lateinit var instance: App

        fun getString(resId: Int): String {
            return instance.getString(resId)
        }

        fun getString(resId: Int, vararg formatArgs: Any): String {
            return instance.getString(resId, *formatArgs)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
} 