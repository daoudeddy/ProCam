package com.googy.procam.ui.viewholder

import android.view.View
import android.view.ViewGroup
import com.googy.procam.R
import com.googy.procam.di.Preferences
import com.googy.procam.ui.adapter.Adapter
import com.googy.procam.ui.model.CheckboxItem
import kotlinx.android.synthetic.main.checkbox_item.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class CheckboxViewHolder(parent: ViewGroup) : BaseViewHolder<CheckboxItem>(R.layout.checkbox_item, parent), KoinComponent {

    private val title = itemView.titleView
    private val summary = itemView.summaryView
    private val checkBox = itemView.checkboxView
    private val subItemsRecycler = itemView.subItemsRecycler

    private val adapter = Adapter()

    private val preferences: Preferences by inject()

    override fun bindView(item: CheckboxItem) {
        title.setText(item.title)
        summary.setText(item.summary)

        checkBox.isChecked = preferences.get(item.key, false)
        subItemsRecycler.visibility = if (checkBox.isChecked) View.VISIBLE else View.GONE

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            preferences.putBoolean(item.key, isChecked)
            subItemsRecycler.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        item.subItems?.let {
            subItemsRecycler.adapter = adapter
            adapter.submitList(it)
        }
    }
}