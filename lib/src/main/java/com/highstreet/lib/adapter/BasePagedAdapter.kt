package com.highstreet.lib.adapter

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.collection.SparseArrayCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.highstreet.lib.BaseApplication
import com.highstreet.lib.view.listener.RxView

abstract class BasePagedAdapter<T : BaseItem>(
    private val layoutId: Int,
    diff: DiffUtil.ItemCallback<T> = Diff()
) :
    PagedListAdapter<T, ViewHolder>(diff) {

    private val strings: SparseArrayCompat<String> by lazy {
        SparseArrayCompat()
    }

    var itemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context, parent, layoutId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindViewHolder(holder, getItem(position)!!, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        itemClickListener?.let {
            RxView.click(holder.itemView) {
                it.itemClicked(holder.itemView, getItem(position)!!, position)
            }
        }
        bindViewHolder(holder, getItem(position)!!, position, payloads)
    }

    open fun bindViewHolder(
        holder: ViewHolder,
        item: T,
        position: Int,
        payloads: MutableList<Any>
    ) {
        bindViewHolder(holder, item, position)
    }

    abstract fun bindViewHolder(holder: ViewHolder, item: T, position: Int)

    fun changeItem(position: Int) {
        if (position in 0 until itemCount) {
            notifyItemChanged(position)
        }
    }

    fun changeItem(position: Int, payload: Any?) {
        if (position in 0 until itemCount) {
            notifyItemChanged(position, payload)
        }
    }

    protected fun getString(@StringRes resId: Int): String {
        var s = strings.get(resId)
        if (s == null) {
            s = BaseApplication.instance.getString(resId)
            strings.append(resId, s)
        }
        return s
    }
}