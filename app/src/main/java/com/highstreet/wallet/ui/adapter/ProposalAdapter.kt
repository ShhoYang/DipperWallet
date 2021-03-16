package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemProposalBinding
import com.highstreet.wallet.model.res.Proposal

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class ProposalAdapter : BasePagedAdapter<ItemProposalBinding, Proposal>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemProposalBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemProposalBinding>,
        item: Proposal,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvId.text = "# " + item.id
            tvTitle.text = item.content?.value?.title
            tvDesc.text = item.content?.value?.description
            tvState.text = item.getState(root.context)
            statePoint.setBackgroundResource(if (item.isPassed()) R.drawable.shape_circle_green else R.drawable.shape_circle_red)
        }
    }
}