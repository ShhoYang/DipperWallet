package com.highstreet.wallet.ui.fragment

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseFragment
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.databinding.FragmentWalletBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.activity.ProposalActivity
import com.highstreet.wallet.ui.activity.TransactionActivity
import com.highstreet.wallet.ui.activity.ValidatorActivity
import com.highstreet.wallet.ui.vm.WalletVM
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.JumpUtils
import com.highstreet.wallet.view.QRDialog

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding, WalletVM>() {

    private var account: Account? = null

    private var amount = ""

    override fun initView() {
        viewBinding {
            RxView.click(ivWalletAddress, showQr)
            RxView.click(llDelegate, ValidatorActivity::class.java, toActivity)
            RxView.click(llVote, ProposalActivity::class.java, toActivity)
            RxView.click(tvHome, toDip)
            RxView.click(tvMedium, toDip)
            RxView.click(cvTransaction, TransactionActivity::class.java, toActivity)
            baseRefreshLayout.isRefreshing = true
            tvCoinUnit.text = "DIP"
            tvAverageYield.text = "95.29%"
            baseRefreshLayout.setOnRefreshListener {
                loadData()
            }
        }
    }

    override fun initData() {
        AccountManager.instance().balanceLiveData.observe(this) {
            loadData()
        }
        AccountManager.instance().currencyLiveData.observe(this) {
            updateAmount()
        }
        Db.instance().accountDao().queryFirstUserAsLiveData().observe(this) {
            account = it
            vb?.tvWalletAddress?.text = account?.address
            loadData()
        }
        viewModel {
            amountLD.observe(this@WalletFragment) {
                amount = it?.getAmount() ?: AmountUtils.ZERO
                updateAmount()
                stopRefresh()
            }
            delegationAmountLD.observe(this@WalletFragment) {
                vb?.tvDelagated?.text = it
                stopRefresh()
            }
            unbondingDelegationAmountLD.observe(this@WalletFragment) {
                vb?.tvUnbondingDelegationAmount?.text = it
                stopRefresh()
            }
            rewardD.observe(this@WalletFragment) {
                vb?.tvReward?.text = it
                stopRefresh()
            }
            inflationD.observe(this@WalletFragment) {
                vb?.tvInflation?.text = it
                stopRefresh()
            }
        }
    }

    private fun loadData() {
        account?.let {
            vm?.loadData(it.address)
        }
    }

    private fun stopRefresh() {
        vb?.baseRefreshLayout?.stopRefresh()
    }

    private fun updateAmount() {
        viewBinding {
            tvAmount.text = amount
            tvAvailable.text = amount
            tvCurrentPrice.text = AmountUtils.getPrice()
            tvAmountValue.text = AmountUtils.getAmountValue(amount)
        }
    }

    private val showQr = {
        act { activity ->
            account?.let {
                QRDialog(activity).show(it.nickName, it.address)
            }
        }
    }

    private val toDip = {
        act {
            JumpUtils.toDipperNetwork(it)
        }
    }
}