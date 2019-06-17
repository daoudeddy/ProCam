package com.googy.procam.xposedhook

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.googy.procam.R


class Button(private val activity: Activity) : View.OnTouchListener {

    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager

    private var initial: Position? = null

    private var pref = activity.getSharedPreferences("custom_camera_pref", MODE_PRIVATE)
    lateinit var parent: ViewGroup

    fun init(layoutRes: Int? = null, arrowUpId: Int? = null, arrowDownId: Int? = null) {
        parent = activity.layoutInflater.inflate(layoutRes ?: R.layout.floating_button, null) as ViewGroup

        params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            gravity = Gravity.CENTER
        }

        Log.e("ProCams", "button $parent")

        parent.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )

        parent.childAt<ImageButton>(0)?.apply {
            val arrowUp = ContextCompat.getDrawable(activity, arrowUpId ?: R.drawable.ic_close_24dp)
            setImageDrawable(arrowUp)
            setOnClickListener {
                handleOnDestroy()
            }
            setOnTouchListener(this@Button)
        }

        windowManager = activity.getSystemService<WindowManager>(WindowManager::class.java)

        Log.e("ProCams", "button $windowManager")

        windowManager.addView(parent, params)
        Log.e("ProCams", "button done")

        pref.edit().putBoolean("overlay", true).apply()
    }

    fun handleOnDestroy() {
        windowManager.removeView(parent)
        pref.edit().putBoolean("overlay", false).apply()
    }

    private inline fun <reified T> ViewGroup.childAt(index: Int): T? {
        return if (index > childCount) null else getChildAt(index) as T
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initial = params.position - event.position
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (event.eventTime - event.downTime < 200) {
                    handleOnDestroy()
                }
                initial = null
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                initial?.let {
                    params.position = it + event.position
                    windowManager.updateViewLayout(parent, params)
                }
                return true
            }
        }
        return false
    }

    private val MotionEvent.position: Position
        get() = Position(rawX, rawY)

    private var WindowManager.LayoutParams.position: Position
        get() = Position(x.toFloat(), y.toFloat())
        set(value) {
            x = value.x
            y = value.y
        }

    private data class Position(val fx: Float, val fy: Float) {

        val x: Int
            get() = fx.toInt()

        val y: Int
            get() = fy.toInt()

        operator fun plus(p: Position) = Position(fx + p.fx, fy + p.fy)
        operator fun minus(p: Position) = Position(fx - p.fx, fy - p.fy)
    }
}