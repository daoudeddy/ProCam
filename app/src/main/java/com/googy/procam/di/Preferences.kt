package com.googy.procam.di

import android.content.Context
import android.content.SharedPreferences
import java.io.File

class Preferences(
    context: Context
) {
    val preferences: SharedPreferences = context.getSharedPreferences("storeBox", Context.MODE_PRIVATE)

    private val file = File("${context.applicationInfo.dataDir}/shared_prefs/", "storeBox.xml")

    private val editor: SharedPreferences.Editor = preferences.edit()

    private var block: () -> Unit = {}

    init {
        externalFile.mkdirs()
    }

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            block()
        }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
        save()
    }

    fun putInteger(key: String, value: Int) {
        editor.putInt(key, value).commit()
        save()
    }

    private fun save() {
        file.copyTo(externalFile, true)
    }

    inline fun <reified T> get(key: String, default: T): T {
        return preferences.all[key] as? T ?: default
    }

    fun onChange(block: () -> Unit) {
        this.block = block
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        val externalFile = File("/storage/emulated/0/Android/data/com.googy.procam/files/shared_prefs/storeBox.xml")
    }

    object Keys {
        const val NIGHT_MODE = "NIGHT_MODE"
    }
}