package com.googy.procam.ui.model

import androidx.annotation.StringRes

data class SeekBarItem(
        val key: String,
        @StringRes val title: Int,
        val minValue: Int,
        val maxValue: Int
) : Item() {
    override fun equals(other: Item): Boolean {
        return other is SeekBarItem && other.title == title && other.maxValue == maxValue && other.minValue == minValue
    }

    override fun getViewType(): Int = 6

    override fun getId(): String {
        return title.toString()
    }
}