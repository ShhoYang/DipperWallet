package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.webkit.WebView
import com.google.gson.Gson
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.L
import com.hao.library.view.web.ProgressAnimHelper
import com.hao.library.view.web.WebViewLoadListener
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.dapp.Trust
import com.highstreet.wallet.dapp.entity.Address
import com.highstreet.wallet.dapp.entity.Message
import com.highstreet.wallet.dapp.entity.Transaction
import com.highstreet.wallet.dapp.entity.TypedData
import com.highstreet.wallet.dapp.sign.*
import com.highstreet.wallet.dapp.web3view.*
import com.highstreet.wallet.databinding.ActivityDappBinding

/**
 * @author Yang Shihao
 * @Date 2/25/21
 */
@AndroidEntryPoint(injectViewModel = false)
class DAppActivity : BaseActivity<ActivityDappBinding, PlaceholderViewModel>(), WebViewLoadListener,
    OnSignTransactionListener,
    OnSignMessageListener,
    OnSignPersonalMessageListener,
    OnSignTypedMessageListener {

    private var progressAnimHelper: ProgressAnimHelper? = null
    private var callSignTransaction: Call<SignTransactionRequest>? = null
    private var callSignMessage: Call<SignMessageRequest>? = null
    private var callSignPersonalMessage: Call<SignPersonalMessageRequest>? = null
    private var callSignTypedMessage: Call<SignTypedMessageRequest>? = null

    override fun initView() {
        viewBinding {
            ivClose.setOnClickListener { finish() }
            tvTitle.text = intent.getStringExtra(ExtraKey.STRING)
            progressAnimHelper = ProgressAnimHelper(progressBar)
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
            baseWebView.apply {
                setWebViewLoadListener(this@DAppActivity)
                chainId = 1
                setRpcUrl(BuildConfig.BASE_URL)
                setWalletAddress(Address(AccountManager.instance().address))
                setOnSignTransactionListener { transaction ->
                    callSignTransaction =
                        Trust.signTransaction().transaction(transaction).call(this@DAppActivity)
                    L.d(TAG, "setOnSignTransactionListener $transaction ")
                }
                setOnSignMessageListener { message ->
                    callSignMessage = Trust.signMessage().message(message).call(this@DAppActivity)
                    L.d(TAG, "setOnSignMessageListener $message ")
                }
                setOnSignPersonalMessageListener { message ->
                    callSignPersonalMessage =
                        Trust.signPersonalMessage().message(message).call(this@DAppActivity)
                    L.d(TAG, "setOnSignPersonalMessageListener $message ")
                }
                setOnSignTypedMessageListener { message ->
                    callSignTypedMessage =
                        Trust.signTypedMessage().message(message).call(this@DAppActivity)
                    L.d(TAG, "setOnSignTypedMessageListener $message ")
                }

                val url = intent.getStringExtra(ExtraKey.STRING_2)
                if (!TextUtils.isEmpty(url)) {
                    loadUrl(url!!)
                }
            }
        }
    }

    override fun initData() {

    }

    override fun onSignTransaction(transaction: Transaction) {
        val s = StringBuilder()
            .append(if (transaction.recipient == null) "" else transaction.recipient.toString())
            .append(" : ")
            .append(if (transaction.contract == null) "" else transaction.contract.toString())
            .append(" : ")
            .append(transaction.value.toString()).append(" : ")
            .append(transaction.gasPrice.toString()).append(" : ")
            .append(transaction.gasLimit).append(" : ")
            .append(transaction.nonce).append(" : ")
            .append(transaction.payload).append(" : ")
            .toString()
        toast(s)
        vb?.baseWebView?.onSignCancel(transaction)
    }

    override fun onSignMessage(message: Message<String>) {
        toast(message.value)
        vb?.baseWebView?.onSignCancel(message)
    }

    override fun onSignPersonalMessage(message: Message<String>) {
        toast(message.value)
        vb?.baseWebView?.onSignCancel(message)
    }

    override fun onSignTypedMessage(message: Message<Array<TypedData>>) {
        toast(Gson().toJson(message))
        vb?.baseWebView?.onSignCancel(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        webView.onSignTransactionSuccessful(transaction, response.result)
//        webView.onSignCancel(transaction)
//        webView.onSignError(transaction, "Some error")
    }

    override fun onDestroy() {
        progressAnimHelper?.destroy()
        vb?.baseWebView?.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (vb?.baseWebView != null && vb!!.baseWebView.canGoBack()) {
            vb!!.baseWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun pageFinished() {
    }

    override fun pageLoadError() {
    }

    override fun progressChanged(newProgress: Int) {
        progressAnimHelper?.progressChanged(newProgress)
    }

    companion object {
        private const val TAG = "--DAppActivity--"

        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, DAppActivity::class.java)
            intent.putExtra(ExtraKey.STRING, title)
            intent.putExtra(ExtraKey.STRING_2, url)
            context.startActivity(intent)
        }
    }
}