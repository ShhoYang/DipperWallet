package com.highstreet.wallet.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseFragment
import com.hao.library.utils.L
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.databinding.FragmentWalletBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.activity.ProposalActivity
import com.highstreet.wallet.ui.activity.StakingActivity
import com.highstreet.wallet.ui.activity.TransactionActivity
import com.highstreet.wallet.ui.vm.CapitalVM
import com.highstreet.wallet.utils.JumpUtils
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.QRDialog
import com.highstreet.wallet.view.listener.RxView
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding, CapitalVM>(), View.OnClickListener {

    private var account: Account? = null

    private var amount = ""
    private var delegationAmount = ""

    private var refresh: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh = AccountManager.instance().refresh
    }

    override fun initView() {
        viewBinding {
            RxView.click(ivWalletAddress, this@WalletFragment)
            RxView.click(llDelegate, this@WalletFragment)
            RxView.click(llVote, this@WalletFragment)
            RxView.click(tvHome, this@WalletFragment)
            RxView.click(tvMedium, this@WalletFragment)
            RxView.click(cvTransaction, this@WalletFragment)
            baseRefreshLayout.setOnRefreshListener {
                loadData()
            }
            baseRefreshLayout.isRefreshing = true
            tvCoinUnit.text = "DIP"
            tvInflation.text = "5.22%"
            tvAverageYield.text = "95.29%"
        }
    }

    override fun initData() {
        viewModel {
            amountLD.observe(this@WalletFragment, Observer {
                val a = it?.getAmount()
                if (!TextUtils.isEmpty(a)) {
                    amount = a!!
                    updateUIStyle()
                }
                vb?.baseRefreshLayout?.stopRefresh()
            })
            delegationAmountLD.observe(this@WalletFragment, Observer {
                if (!TextUtils.isEmpty(it)) {
                    delegationAmount = it
                    updateUIStyle()
                }
                vb?.baseRefreshLayout?.stopRefresh()
            })
        }
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            account = it
            loadData()
        })
    }

    private fun loadData() {
        account?.let {
            viewModel {
                getAccountInfo(it.address)
                getDelegationAmount(it.address)
            }
        }
    }

    private fun updateUIStyle() {
        viewBinding {
            tvWalletAddress.text = account?.address
            val amount = StringUtils.pdip2DIP(amount, false)
            tvAmount.text = amount
            tvCash.text = amount
            tvAvailable.text = amount
            tvDelagated.text = StringUtils.pdip2DIP(delegationAmount, false)
            tvUnbonding.text = ""
            tvReward.text = ""
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                ivWalletAddress -> {
                    act { activity ->
                        account?.let {
                            QRDialog(activity).show(it.nickName, it.address)
                        }
                    }
                }
                llDelegate -> toA(StakingActivity::class.java)
                llVote -> toA(ProposalActivity::class.java)
                tvHome, tvMedium -> act {
                    JumpUtils.toDipperNetwork(it)
                }
                cvTransaction -> toA(TransactionActivity::class.java)
            }
        }
    }
}