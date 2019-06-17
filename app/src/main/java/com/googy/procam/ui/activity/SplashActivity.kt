package com.googy.procam.ui.activity

import android.content.Intent
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : BaseActivity() {
    override fun toolbarVisible() = false

//    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        timer.schedule(0L) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        timer.cancel()
//        timer.purge()
    }
}
