package com.googy.procam.ui.model


data class GridItem<T>(
    val identifier: String,
    val items: List<T>
) : Item() {
    override fun getId() = identifier
    override fun getViewType() = 4
    override fun equals(other: Item) = other is GridItem<*>
}