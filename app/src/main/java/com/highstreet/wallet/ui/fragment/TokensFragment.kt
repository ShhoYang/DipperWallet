package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListFragment
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.cache.BalanceCache
import com.highstreet.wallet.databinding.FragmentTokensBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.db.Token
import com.highstreet.wallet.ui.activity.NativeTokenDetailActivity
import com.highstreet.wallet.ui.activity.TokenDetailActivity
import com.highstreet.wallet.ui.adapter.MyTokensAdapter
import com.highstreet.wallet.ui.vm.TokensVM
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class TokensFragment :
    BaseNormalListFragment<FragmentTokensBinding, Token, TokensVM, MyTokensAdapter>() {

    private var account: Account? = null

    override fun initData() {
        vm?.amountLD?.observe(this, {
            viewBinding {
                baseRefreshLayout.stopRefresh()
                val amount = it?.getAmount()
                tvAmount.text = amount ?: AmountUtils.ZERO
                tvAmountValue.text = AmountUtils.getAmountValue(amount)
            }
            loadTokenBalance(adapter.data)
            adapter.notifyDataSetChanged()
        })

        AccountManager.instance().balanceLiveData.observe(this) {
            onRefresh()
        }
        AccountManager.instance().currencyLiveData.observe(this) {
            onRefresh()
        }
        Db.instance().accountDao().queryFirstUserAsLiveData().observe(this) {
            account = it
            loadToken(arrayListOf())
            onRefresh()
        }
        Db.instance().tokenDao().queryAllAsLiveData().observe(this) {
            loadToken(it)
        }
    }

    private fun loadToken(list: List<Token>) {
        val ret = ArrayList<Token>()
        account?.apply {
            ret.add(
                Token(
                    id = null,
                    isNative = true,
                    chain = chain,
                    icon = "",
                    name = "DIP",
                    desc = "Dipper Network Native Token",
                    decimalPlaces = 6,
                    address = address,
                    symbol = "DIP",
                    balance = "",
                    balanceAmount = "",
                    delegateAmount = "",
                    unbondingAmount = "",
                    reward = "",
                    extension = ""
                )
            )

//            ret.add(
//                Token(
//                    id = null,
//                    isNative = false,
//                    chain = "DIPSYN",
//                    icon = "",
//                    name = "DIPSYN",
//                    desc = "DIPSYN",
//                    decimalPlaces = 6,
//                    address = address,
//                    symbol = "DIPSYN",
//                    balance = "",
//                    balanceAmount = "",
//                    delegateAmount = "",
//                    unbondingAmount = "",
//                    reward = "",
//                    extension = ""
//                )
//            )
        }
        ret.addAll(list)
        loadTokenBalance(ret)
        adapter.resetData(ret)
    }

    private fun loadTokenBalance(list: List<Token>) {
        list.forEach {
            it.balance = BalanceCache.instance().getBalance(it)
            it.balanceAmount = AmountUtils.getAmountValue(it.balance)
        }
    }

    override fun onRefresh() {
        account?.let {
            viewModel {
                getAccountInfo(it.address)
            }
        }
    }

    override fun itemClicked(view: View, item: Token, position: Int) {
        act {
            if (item.isNative) {
                NativeTokenDetailActivity.start(it, item)
            } else {
                TokenDetailActivity.start(it, item)
            }
        }
    }
}