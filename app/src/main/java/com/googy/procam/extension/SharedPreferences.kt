package com.googy.procam.extension

import android.content.SharedPreferences

inline fun <reified T> SharedPreferences.get(key: String, default: T): T {
    return all[key] as? T ?: default
}