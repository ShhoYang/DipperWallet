package com.highstreet.lib.adapter

import androidx.recyclerview.widget.DiffUtil


/**
 * @author Yang Shihao
 * @Date 2020-01-14
 */
class Diff<T : BaseItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(item: T, item1: T) = item.uniqueKey() == item1.uniqueKey()

    override fun areContentsTheSame(item: T, item1: T) = item.uniqueKey() == item1.uniqueKey()
}