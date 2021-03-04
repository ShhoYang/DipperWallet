package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.PagedAdapterItem
import com.highstreet.wallet.databinding.ItemCommonBinding

/**
 * @author Yang Shihao
 * @Date 2/26/21
 */
abstract class CommonAdapter<D : PagedAdapterItem> : BasePagedAdapter<ItemCommonBinding, D>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemCommonBinding.inflate(layoutInflater, parent, false)
}