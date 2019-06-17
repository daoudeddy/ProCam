package com.googy.procam.ui.viewholder

import android.view.ViewGroup
import com.googy.procam.ui.model.EmptyItem
import com.googy.procam.ui.model.Item
import com.googy.procam.ui.viewholder.BaseViewHolder
import com.googy.procam.R

class EmptyViewHolder(parent: ViewGroup) : BaseViewHolder<EmptyItem>(R.layout.empty, parent) {
    override fun bindView(item: EmptyItem) {

    }
}
