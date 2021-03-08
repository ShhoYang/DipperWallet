package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseWebActivity
import com.hao.library.view.web.WebViewLoadListener
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityWebBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint(injectViewModel = false)
class WebActivity : BaseWebActivity<ActivityWebBinding, PlaceholderViewModel>(),
    WebViewLoadListener {

    override fun initView() {
        super.initView()
        title = intent.getStringExtra(ExtraKey.STRING)
        load(intent.getStringExtra(ExtraKey.STRING_2) ?: "")
    }

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(ExtraKey.STRING, title)
            intent.putExtra(ExtraKey.STRING_2, url)
            context.startActivity(intent)
        }
    }
}