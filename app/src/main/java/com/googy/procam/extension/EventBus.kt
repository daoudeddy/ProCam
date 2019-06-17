package com.googy.procam.extension

import android.app.Activity
import org.greenrobot.eventbus.EventBus

fun Activity.registerBus() {
    try {
        EventBus.getDefault().register(this)
    } catch (e: Exception) {
    }
}

fun Activity.unregisterBus() {
    try {
        EventBus.getDefault().unregister(this)
    } catch (e: Exception) {
    }
}

fun Any.postToBus(){
    EventBus.getDefault().post(this)
}