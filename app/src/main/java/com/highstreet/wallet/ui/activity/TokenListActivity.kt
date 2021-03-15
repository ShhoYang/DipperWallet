package com.highstreet.wallet.ui.activity

import com.hao.library.ui.BaseNormalListActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityTokenListBinding
import com.highstreet.wallet.model.res.Token
import com.highstreet.wallet.ui.adapter.TokenAdapter
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
class TokenListActivity :
    BaseNormalListActivity<ActivityTokenListBinding, Token, PlaceholderViewModel, TokenAdapter>() {

    override fun initView() {
        super.initView()
        viewBinding {
            RxView.click(btnAdd) {
                toA(AddTokenActivity::class.java)
            }
        }
    }

    override fun initData() {
        val list = ArrayList<Token>()
        list.add(Token("", "DIP", "0"))
        adapter.resetData(list)
    }
}