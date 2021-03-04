package com.highstreet.wallet.ui.activity

import android.annotation.SuppressLint
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityAgreementBinding
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class AgreementActivity : BaseActivity<ActivityAgreementBinding, PlaceholderViewModel>() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        toolbarLayout {
            showBack(false)
            title = "服务协议"
        }
        viewBinding {
            baseWebView.loadUrl("https://github.com/haoshiy/Wallet")
            cbAgree.setOnCheckedChangeListener { _, isChecked ->
                btnNext.isEnabled = isChecked
            }
            RxView.click(btnNext) {
                toA(InitWalletActivity::class.java, true)
            }
        }
    }

    override fun initData() {
    }
}
