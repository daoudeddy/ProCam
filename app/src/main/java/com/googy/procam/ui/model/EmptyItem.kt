package com.googy.procam.ui.model

import java.util.*

class EmptyItem : Item() {
    override fun getId() = UUID.randomUUID().toString()
    override fun equals(other: Item) = false
    override fun getViewType() = 0
}