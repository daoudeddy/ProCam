package com.googy.procam.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.googy.procam.di.Preferences
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@App); modules(prefModule) }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private val prefModule = module {
        single { Preferences(applicationContext) }
    }
}