package com.googy.procam.ui.viewholder

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.googy.procam.ui.model.TitleDividerItem
import com.googy.procam.R

class TitleDividerViewHolder(parent: ViewGroup) : BaseViewHolder<TitleDividerItem>(R.layout.title_divider, parent) {

    private val title: AppCompatTextView by lazy { itemView.findViewById<AppCompatTextView>(R.id.titleView) }

    override fun bindView(item: TitleDividerItem) {
        title.setText(item.title)
    }
}