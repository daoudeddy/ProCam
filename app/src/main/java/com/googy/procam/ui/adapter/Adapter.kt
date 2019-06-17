package com.googy.procam.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.googy.procam.ui.model.Item
import com.googy.procam.ui.viewholder.BaseViewHolder
import com.googy.procam.ui.viewholder.ViewHolderFactory

internal class Adapter : ListAdapter<Item, BaseViewHolder<Item>>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        holder.bindView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Item> {
        return ViewHolderFactory.getViewHolder(parent, viewType)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Item> = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                oldItem: Item, newItem: Item
            ): Boolean {
                return oldItem.getId() === newItem.getId()
            }

            override fun areContentsTheSame(
                oldItem: Item, newItem: Item
            ): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}