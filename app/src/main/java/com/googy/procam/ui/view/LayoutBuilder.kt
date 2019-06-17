package com.googy.procam.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView

class LayoutBuilder(val context: Context) {
    fun verticalLayout(init: LinearLayout.() -> Unit) = LinearLayout(context).apply {
        layoutParams = defaultLayoutParam()
        init()
        orientation = LinearLayout.VERTICAL
    }

    fun horizontalLayout(init: LinearLayout.() -> Unit) = LinearLayout(context).apply {
        init()
        orientation = LinearLayout.HORIZONTAL
    }

    fun LinearLayout.textView(init: AppCompatTextView.() -> Unit) = textView().applyParams(init).also(::addView)
    fun LinearLayout.button(init: AppCompatButton.() -> Unit) = button().applyParams(init).also(::addView)
    fun LinearLayout.imageButton(init: AppCompatImageButton.() -> Unit) = imageButton().applyParams(init).also(::addView)

    fun View.lparams(init: LinearLayout.LayoutParams.() -> Unit) = apply {
        (layoutParams as LinearLayout.LayoutParams).apply(init)
    }

    private inline fun <reified T : View> T.applyParams(init: T.() -> Unit): T {
        layoutParams = defaultLayoutParam(); init(); return this
    }

    private fun Context.dip(px: Int): Int = (px * resources.displayMetrics.density).toInt()

    fun View.dip(px: Int) = context.dip(px)

    private fun defaultLayoutParam() = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
    )

    private fun textView(): AppCompatTextView {
        return AppCompatTextView(context.themedContext(android.R.style.Widget_Material_TextView))
    }

    private fun button(): AppCompatButton {
        return AppCompatButton(context.themedContext(android.R.style.Widget_Material_Button_Borderless_Colored))
    }

    private fun imageButton(): AppCompatImageButton {
        return AppCompatImageButton(context)
    }

}

fun Context.themedContext(@StyleRes themeResId: Int) = ContextThemeWrapper(this, themeResId)
