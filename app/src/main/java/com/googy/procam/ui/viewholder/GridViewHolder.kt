package com.googy.procam.ui.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.googy.procam.ui.adapter.Adapter
import com.googy.procam.ui.decorator.SpacesItemDecoration
import com.googy.procam.ui.model.AboutItem
import com.googy.procam.ui.model.GridItem
import com.googy.procam.R

class GridViewHolder(parent: ViewGroup) : BaseViewHolder<GridItem<AboutItem>>(R.layout.grid_item, parent) {

    private val adapter = Adapter()
    private val gridView = itemView.findViewById<RecyclerView>(R.id.gridView).apply {
        addItemDecoration(SpacesItemDecoration(10))
        adapter = this@GridViewHolder.adapter
    }

    override fun bindView(item: GridItem<AboutItem>) {
        gridView.layoutManager = GridLayoutManager(itemView.context, 6).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val span = if (spanCount > itemCount) spanCount / 2 else spanCount
                    val ref = itemCount / span * span
                    return if (position >= ref) {
                        when (itemCount - ref) {
                            1 -> 6
                            2 -> 3
                            else -> 2
                        }
                    } else {
                        2
                    }
                }
            }
        }
        adapter.submitList(item.items)
    }
}