package com.googy.procam.ui.model

import androidx.annotation.StringRes

data class TitleDividerItem(
    @StringRes val title: Int
) : Item() {
    override fun getViewType() = 3
    override fun getId() = title.toString()

    override fun equals(other: Item): Boolean {
        return other is TitleDividerItem && other.title == title
    }
}