package com.googy.procam.ui.model

import androidx.annotation.StringRes

data class AboutItem(
        @StringRes val title: Int,
        val url: String,
        val icon: Int
) : Item() {
    override fun getId(): String = title.toString()

    override fun equals(other: Item) = other is AboutItem && other.title == title && other.url == url

    override fun getViewType() = 5
}