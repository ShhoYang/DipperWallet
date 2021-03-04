package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.databinding.FragmentProposalBinding
import com.highstreet.wallet.model.res.Proposal
import com.highstreet.wallet.ui.activity.ProposalDetailActivity
import com.highstreet.wallet.ui.adapter.ProposalAdapter
import com.highstreet.wallet.ui.vm.ProposalVM


/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
@AndroidEntryPoint
class ProposalFragment :
    BaseListFragment<FragmentProposalBinding, Proposal, ProposalVM, ProposalAdapter>() {

    override fun itemClicked(view: View, item: Proposal, position: Int) {
        activity?.apply { ProposalDetailActivity.start(this, item) }
    }
}