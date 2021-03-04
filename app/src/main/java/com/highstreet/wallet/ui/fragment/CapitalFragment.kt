package com.highstreet.wallet.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseFragment
import com.hao.library.view.dialog.ConfirmDialog
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.databinding.FragmentCapitalBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.activity.ReceiveActivity
import com.highstreet.wallet.ui.activity.TransactionActivity
import com.highstreet.wallet.ui.activity.TransactionRecordActivity
import com.highstreet.wallet.ui.activity.WalletManageActivity
import com.highstreet.wallet.ui.vm.CapitalVM
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.listener.RxView
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

@AndroidEntryPoint
class CapitalFragment : BaseFragment<FragmentCapitalBinding, CapitalVM>(), View.OnClickListener {

    private var account: Account? = null

    private var amount = ""
    private var delegationAmount = ""

    private var refresh: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            loadData()
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(CapitalVM::class.java)
    }

    override fun onResume() {
        super.onResume()
        refresh = AccountManager.instance().refresh
    }

    override fun initView() {
        viewBinding {
            baseRefreshLayout.setOnRefreshListener {
                loadData()
            }

            ivEye.isSelected = false
            RxView.click(tvWalletAddress, this@CapitalFragment)
            RxView.click(ivWalletAddress, this@CapitalFragment)
            RxView.click(ivEye, 300, this@CapitalFragment)

            RxView.click(ivSwitchWallet, this@CapitalFragment)
            RxView.click(ivTip, this@CapitalFragment)
            RxView.click(flTransaction, this@CapitalFragment)
            RxView.click(flTransactionRecord, this@CapitalFragment)
            RxView.click(ivTip, this@CapitalFragment)
            baseRefreshLayout.isRefreshing = true
        }
    }

    override fun initData() {
        viewModel.amountLD.observe(this, Observer {
            val a = it?.getAmount()
            if (!TextUtils.isEmpty(a)) {
                amount = a!!
                viewBinding {
                    updateUIStyle(ivEye.isSelected)
                }
            }
            vb?.baseRefreshLayout?.stopRefresh()
        })
        viewModel.delegationAmountLD.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                delegationAmount = it
                viewBinding {
                    updateUIStyle(ivEye.isSelected)
                }
            }
            vb?.baseRefreshLayout?.stopRefresh()
        })
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            account = it
            loadData()
        })
    }

    private fun loadData() {
        account?.let {
            vb?.tvChainName?.text = it.getUpperCaseChainName()
            viewModel.getAccountInfo(it.address)
            viewModel.getDelegationAmount(it.address)
        }
    }

    private fun updateUIStyle(isSelected: Boolean) {
        viewBinding {
            tvWalletName.text = account?.nickName
            tvWalletAddress.text = account?.address
            ivEye.isSelected = isSelected
            if (isSelected) {
                tvAmount.text = "****************"
                tvAvailableAmount.text = "********"
                tvDelegationAmount.text = "********"
                ivTip.gone()
            } else {
                tvAmount.text = StringUtils.pdip2DIP(amount)
                tvAvailableAmount.text = StringUtils.pdip2DIP(amount)
                tvDelegationAmount.text = StringUtils.pdip2DIP(delegationAmount)
                ivTip.visible()
            }
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                tvWalletAddress, ivWalletAddress -> toA(ReceiveActivity::class.java)
                ivEye -> viewBinding { updateUIStyle(!ivEye.isSelected) }
                ivSwitchWallet -> toA(WalletManageActivity::class.java)
                ivTip -> {
                    activity?.let {
                        ConfirmDialog.Builder(it)
                            .setMessage("1DIP = 1,000,000,000,000pdip")
                            .showCancelBtn(false)
                            .build()
                            .show()
                    }
                }
                flTransaction -> toA(TransactionActivity::class.java)
                flTransactionRecord -> toA(TransactionRecordActivity::class.java)
            }
        }
    }
}