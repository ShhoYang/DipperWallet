package com.highstreet.wallet.ui.fragment

import androidx.lifecycle.Observer
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.databinding.FragmentTokensBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.res.Token
import com.highstreet.wallet.ui.adapter.TokenAdapter
import com.highstreet.wallet.ui.vm.TokensVM
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class TokensFragment : BaseListFragment<FragmentTokensBinding, Token, TokensVM, TokenAdapter>() {

    private var account: Account? = null

    override fun initData() {
        super.initData()
        vm?.amountLD?.observe(this, {
            viewBinding {
                tvAmount.text = StringUtils.pdip2DIP(it.getAmount(), false)
            }
        })
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            account = it
            loadData()
        })
    }

    private fun loadData() {
        account?.let {
            viewModel {
                getAccountInfo(it.address)
            }
        }
    }
}