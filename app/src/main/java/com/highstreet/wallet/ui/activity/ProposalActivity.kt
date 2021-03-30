package com.highstreet.wallet.ui.activity

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.ActivityBaseListBinding
import com.hao.library.ui.BaseListActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.model.res.Proposal
import com.highstreet.wallet.ui.adapter.ProposalAdapter
import com.highstreet.wallet.ui.vm.ProposalVM


/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint
class ProposalActivity :
    BaseListActivity<ActivityBaseListBinding, Proposal, ProposalVM, ProposalAdapter>() {

    override fun initView() {
        setTitle(R.string.pa_proposalList)
        super.initView()
    }

    override fun itemClicked(view: View, item: Proposal, position: Int) {
        ProposalDetailActivity.start(this, item)
    }
}