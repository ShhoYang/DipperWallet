package com.highstreet.wallet.ui.fragment

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListFragment
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.databinding.FragmentTokensBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.adapter.MyTokensAdapter
import com.highstreet.wallet.ui.vm.TokensVM
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class MyTokenFragment :
    BaseNormalListFragment<FragmentTokensBinding, Account, TokensVM, MyTokensAdapter>() {

    private var account: Account? = null

    override fun initData() {
        vm?.amountLD?.observe(this, {
            val amount = it.getAmount()
            viewBinding {
                baseRefreshLayout.stopRefresh()
                tvAmount.text = amount
                tvAmountValue.text = AmountUtils.getAmountValue(amount)
            }
        })

        AccountManager.instance().balanceLiveData.observe(this) {
            onRefresh()
        }
        AccountManager.instance().currencyLiveData.observe(this) {
            onRefresh()
        }
        Db.instance().accountDao().queryFirstUserAsLiveData()
            .observe(this) {
                account = it
                onRefresh()
            }
        Db.instance().accountDao().queryAllByChainAsLiveData(AccountManager.instance().chain)
            .observe(this) {
//                adapter.resetData(it)
            }

        adapter.resetData(arrayListOf())
    }

    override fun onRefresh() {
        account?.let {
            viewModel {
                getAccountInfo(it.address)
            }
        }
    }
}