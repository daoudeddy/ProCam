package com.googy.procam.ui.viewholder

import android.view.ViewGroup


object ViewHolderFactory {
    inline fun <reified I, reified T : BaseViewHolder<I>> getViewHolder(parent: ViewGroup, viewType: Int): T {
        return when (viewType) {
            1 -> SwitchViewHolder(parent)
            2 -> CheckboxViewHolder(parent)
            3 -> TitleDividerViewHolder(parent)
            4 -> GridViewHolder(parent)
            5 -> AboutViewHolder(parent)
            6 -> SeekBarViewHolder(parent)
            else -> EmptyViewHolder(parent)
        } as T
    }
}