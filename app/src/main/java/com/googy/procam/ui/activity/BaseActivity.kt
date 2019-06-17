package com.googy.procam.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.google.android.material.snackbar.Snackbar
import com.googy.procam.R
import com.googy.procam.di.Preferences
import com.googy.procam.extension.registerBus
import com.googy.procam.extension.unregisterBus
import kotlinx.android.synthetic.main.activity_base.*
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    private val preferences: Preferences by inject()
    private val rootView: FrameLayout by lazy { findViewById<FrameLayout>(R.id.viewContainer) }

    abstract fun toolbarVisible(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initUIMode()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        if (toolbarVisible()) setSupportActionBar(my_toolbar)
    }

    private fun initUIMode() {
        val nightMode = preferences.get(Preferences.Keys.NIGHT_MODE, -1)
        if (nightMode != -1) {
            delegate.setLocalNightMode(if (nightMode == 1) MODE_NIGHT_YES else MODE_NIGHT_NO)
        } else {
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.dark_theme)?.setIcon(if (isNightMode()) R.drawable.sun else R.drawable.moon)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dark_theme -> {
                if (isNightMode()) {
                    item.setIcon(R.drawable.moon)
                    delegate.setLocalNightMode(MODE_NIGHT_NO)
                    preferences.putInteger(Preferences.Keys.NIGHT_MODE, 0)
                } else {
                    item.setIcon(R.drawable.sun)
                    delegate.setLocalNightMode(MODE_NIGHT_YES)
                    preferences.putInteger(Preferences.Keys.NIGHT_MODE, 1)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun indefiniteActionSnackbar(@StringRes snackText: Int, @StringRes actionText: Int, action: () -> Unit) {
        Snackbar.make(rootView, snackText, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(actionText) { action() }
        }.show()
    }

    public override fun onStart() {
        super.onStart()
        registerBus()
    }

    public override fun onStop() {
        super.onStop()
        unregisterBus()
    }

    fun setView(layoutId: Int) {
        rootView.addView(LayoutInflater.from(this).inflate(layoutId, rootView, false))
    }

    private fun isNightMode(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
