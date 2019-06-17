package com.googy.procam.ui.viewholder

import android.view.View
import android.view.ViewGroup
import com.googy.procam.R
import com.googy.procam.di.Preferences
import com.googy.procam.extension.postToBus
import com.googy.procam.ui.adapter.Adapter
import com.googy.procam.ui.model.SwitchItem
import kotlinx.android.synthetic.main.checkbox_item.view.subItemsRecycler
import kotlinx.android.synthetic.main.checkbox_item.view.summaryView
import kotlinx.android.synthetic.main.checkbox_item.view.titleView
import kotlinx.android.synthetic.main.switch_item.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class SwitchViewHolder(parent: ViewGroup) : BaseViewHolder<SwitchItem>(R.layout.switch_item, parent), KoinComponent {


    private val title = itemView.titleView
    private val summary = itemView.summaryView
    private val switch = itemView.switchView
    private val subItemsRecycler = itemView.subItemsRecycler
    private val adapter = Adapter()

    private val preferences: Preferences by inject()

    override fun bindView(item: SwitchItem) {
        title.setText(item.title)
        summary.setText(item.summary)

        switch.isChecked = preferences.get(item.key, false)
        subItemsRecycler.visibility = if (switch.isChecked) View.VISIBLE else View.GONE

        switch.setOnCheckedChangeListener { _, isChecked ->
            preferences.putBoolean(item.key, isChecked)
            subItemsRecycler.visibility = if (isChecked) View.VISIBLE else View.GONE
            item.postToBus()
        }

        item.subItems?.let {
            subItemsRecycler.adapter = adapter
            adapter.submitList(it)
        }
    }
}