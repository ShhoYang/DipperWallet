package com.highstreet.wallet.ui.activity

import com.hao.library.ui.BaseNormalListActivity
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityTokenBinding
import com.highstreet.wallet.db.Token
import com.highstreet.wallet.ui.adapter.TokenAdapter

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
class TokenActivity :
    BaseNormalListActivity<ActivityTokenBinding, Token, PlaceholderViewModel, TokenAdapter>() {

    override fun initView() {
        super.initView()
        viewBinding {
            RxView.click(btnAdd, AddTokenActivity::class.java, toActivity)
        }
    }

    override fun initData() {
        val list = ArrayList<Token>()
        adapter.resetData(list)
    }
}