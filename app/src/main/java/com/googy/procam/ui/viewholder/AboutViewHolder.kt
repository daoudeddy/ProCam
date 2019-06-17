package com.googy.procam.ui.viewholder

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.googy.procam.R
import com.googy.procam.ui.model.AboutItem

class AboutViewHolder(parent: ViewGroup) : BaseViewHolder<AboutItem>(R.layout.about_item, parent) {

    private val button: MaterialButton = itemView.findViewById(R.id.button)

    override fun bindView(item: AboutItem) {
        button.setText(item.title)
        button.setIconResource(item.icon)
        button.setOnClickListener {
            openUrl(item.url)
        }
    }

    fun openUrl(url: String) {
        itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}