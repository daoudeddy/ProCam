package com.googy.procam.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.googy.procam.R
import com.googy.procam.data.Data
import com.googy.procam.ui.adapter.Adapter
import com.googy.procam.ui.model.Item
import com.googy.procam.ui.model.SwitchItem
import com.googy.procam.xposedhook.Button
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.alert


class MainActivity : BaseActivity() {
    override fun toolbarVisible() = true

    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val adapter = Adapter()
    private val REQUEST_STORAGE_PERMISSION = 420

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.activity_main)
        recyclerView.adapter = adapter

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            alert(R.string.permission_alert_body, R.string.permission_alert_title) {
                positiveButton(android.R.string.ok) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
                }
            }.show()
        } else {
            adapter.submitList(Data.mainData)
        }

    }

    override fun onResume() {
        super.onResume()

        val button = Button(this)
        button.init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    adapter.submitList(Data.mainData)
                } else {
                    indefiniteActionSnackbar(R.string.permission_denied_snack, R.string.enable_it) {
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
                    }
                }
            }
        }
    }

    @Subscribe
    fun onItemClick(item: Item) {
        when (item) {
            is SwitchItem -> {
                if (item.key == Data.Keys.SHUTTER_SCHEDULER) {
                    if (!Settings.canDrawOverlays(this)) {
                        indefiniteActionSnackbar(R.string.enable_appear_on_top, R.string.lets_go) {
                            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 0)
                        }
                    }
                }
            }
        }
    }
}
