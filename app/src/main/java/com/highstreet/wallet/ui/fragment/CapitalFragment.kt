package com.highstreet.wallet.ui.fragment

import androidx.lifecycle.Observer
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.extensions.gone
import com.highstreet.lib.extensions.visible
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.activity.ReceiveActivity
import com.highstreet.wallet.ui.activity.TransactionActivity
import com.highstreet.wallet.ui.activity.TransactionRecordActivity
import com.highstreet.wallet.ui.activity.WalletManageActivity
import com.highstreet.wallet.ui.vm.CapitalVM
import kotlinx.android.synthetic.main.g_fragment_capital.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class CapitalFragment : BaseFragment(), View.OnClickListener {

    private var account: Account? = null

    private var amount = ""
    private var delegationAmount = ""

    private val viewModel by lazy {
        ViewModelProvider(this).get(CapitalVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_fragment_capital

    override fun initView() {

        baseRefreshLayout.setOnRefreshListener {
            loadData()
        }

        ivEye.isSelected = false
        RxView.click(tvWalletAddress, this)
        RxView.click(ivWalletAddress, this)
        RxView.click(ivEye, 300, this)

        RxView.click(ivSwitchWallet, this)
        RxView.click(ivTip, this)
        RxView.click(flTransaction, this)
        RxView.click(flTransactionRecord, this)
        RxView.click(ivTip, this)
        baseRefreshLayout.isRefreshing = true
    }

    override fun initData() {
        viewModel.amountLD.observe(this, Observer {
            val a = it?.getAmount()
            if (!TextUtils.isEmpty(a)) {
                amount = a!!
                updateUIStyle(ivEye.isSelected)
            }
            baseRefreshLayout.stopRefresh()
        })
        viewModel.delegationAmountLD.observe(this, Observer {
            val a = it ?: ""
            if (!TextUtils.isEmpty(a)) {
                delegationAmount = a!!
                updateUIStyle(ivEye.isSelected)
            }
            baseRefreshLayout.stopRefresh()
        })
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            account = it
            loadData()
        })
    }

    private fun loadData() {
        account?.let {
            tvChainName.text = it.getUpperCaseChainName()
            viewModel.getAccountInfo(it.address)
            viewModel.getDelegationAmount(it.address)
        }
    }

    private fun updateUIStyle(isSelected: Boolean) {
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

    override fun onClick(v: View?) {
        when (v) {
            tvWalletAddress, ivWalletAddress -> to(ReceiveActivity::class.java)
            ivEye -> updateUIStyle(!ivEye.isSelected)
            ivSwitchWallet -> to(WalletManageActivity::class.java)
            ivTip -> {
                activity?.let {
                    ConfirmDialog(it).setMsg("1DIP等于1,000,000,000,000pdip")
                        .hideCancel()
                        .show()
                }
            }
            flTransaction -> to(TransactionActivity::class.java)
            flTransactionRecord -> to(TransactionRecordActivity::class.java)
        }
    }

    companion object {
        private const val TAG = "--CapitalFragment--"
    }
}