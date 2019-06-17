package com.googy.procam.ui.model

import androidx.annotation.StringRes

data class CheckboxItem(
        val key: String,
        @StringRes val title: Int,
        @StringRes val summary: Int,
        val subItems: List<Item>? = null
) : Item() {
    override fun getViewType() = 2
    override fun getId() = key

    override fun equals(other: Item): Boolean {
        return other is SwitchItem && other.key == key && other.title == title && other.summary == summary
    }
}