package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityAboutBinding

/**
 * @author Yang Shihao
 * @Date 12/18/20
 */
@AndroidEntryPoint(injectViewModel = false)
class AboutActivity : BaseActivity<ActivityAboutBinding, PlaceholderViewModel>() {

    override fun initView() {
        title = "${getString(R.string.about)} ${getString(R.string.wallet_app_name)}"
        viewBinding {
            if (BuildConfig.testnet) {
                ivLogo.setImageResource(R.mipmap.ic_launcher_2)
            } else {
                ivLogo.setImageResource(R.mipmap.ic_launcher)
            }
            tvVersion.text = "v${packageManager.getPackageInfo(packageName, 0).versionName}"
        }
    }

    override fun initData() {
    }
}