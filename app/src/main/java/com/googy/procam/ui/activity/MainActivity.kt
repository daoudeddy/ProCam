package com.googy.procam.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.googy.procam.R
import com.googy.procam.data.Data
import com.googy.procam.ui.adapter.Adapter
import com.googy.procam.xposedhook.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

class MainActivity : BaseActivity() {

    override fun toolbarVisible() = true

    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.activity_main)
        recyclerView.adapter = adapter
        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            alert(R.string.permission_alert_body, R.string.permission_alert_title) {
                positiveButton(android.R.string.ok) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
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
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 420
    }
}
