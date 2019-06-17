package com.googy.procam.ui.model

abstract class Item {
    abstract fun getId(): String
    abstract fun equals(other: Item) : Boolean
    abstract fun getViewType() : Int
}