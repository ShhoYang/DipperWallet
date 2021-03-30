package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.init
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.CoroutineUtils
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.ui.adapter.MnemonicAdapter
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityBackupVerifyBinding
import com.highstreet.wallet.db.Account

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class BackupVerifyActivity : BaseActivity<ActivityBackupVerifyBinding, PlaceholderViewModel>() {

    private var from = BackupActivity.FROM_CREATE
    private var account: Account? = null
    private var mnemonicS = ""

    private val topList = ArrayList<String>()
    private val bottomList = ArrayList<String>()

    @Inject
    lateinit var topAdapter: MnemonicAdapter

    @Inject
    lateinit var bottomAdapter: MnemonicAdapter

    override fun initView() {
        setTitle(R.string.bva_backupMnemonic)
        from = intent.getIntExtra(ExtraKey.INT, BackupActivity.FROM_CREATE)
        account = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Account?
        val mnemonic = intent.getSerializableExtra(ExtraKey.SERIALIZABLE_2) as ArrayList<String>?
        if (account == null || mnemonic == null || mnemonic.isEmpty()) {
            finish()
            return
        }
        topList.clear()
        bottomList.clear()
        bottomList.addAll(mnemonic)
        mnemonicS = bottomList.joinToString()
        bottomList.shuffle()
        topAdapter.setOnItemClickListener(object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                topList.removeAt(position)
                topAdapter.resetData(topList)
                bottomList.add(item)
                bottomList.shuffle()
                bottomAdapter.resetData(bottomList)

                checkCompleted()
            }
        })
        bottomAdapter.setOnItemClickListener(object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                bottomList.removeAt(position)
                bottomList.shuffle()
                bottomAdapter.resetData(bottomList)

                topList.add(item)
                topAdapter.resetData(topList)
                checkCompleted()
            }
        })
        viewBinding {
            rvTop.init(topAdapter, 4)
            rvBottom.init(bottomAdapter, 4)

            RxView.click(btnConfirm,verify)
        }
        topAdapter.resetData(topList)
        bottomAdapter.resetData(bottomList)

    }

    private fun checkCompleted() {
        vb?.btnConfirm?.isEnabled = topList.size == Constant.MNEMONIC_SIZE
    }

    private val verify = {
        if (mnemonicS == topList.joinToString()) {
            toast(R.string.succeed)
            CoroutineUtils.io2main({
                account!!.isBackup = true
                AccountManager.instance().update(account!!)
            }, {
                if (BackupActivity.FROM_CREATE == from) {
                    toA(MainActivity::class.java, true)
                }
                finish()
            })
        } else {
            toast(R.string.bva_invalidMnemonic)
        }
    }

    override fun initData() {

    }

    override fun onBackPressed() {
        if (BackupActivity.FROM_CREATE == from) {
            toA(MainActivity::class.java, true)
        } else {
            finish()
        }
    }


    companion object {

        fun start(context: Context, from: Int, account: Account, mnemonic: ArrayList<String>) {
            val intent = Intent(context, BackupVerifyActivity::class.java)
            intent.putExtra(ExtraKey.INT, from)
            intent.putExtra(ExtraKey.SERIALIZABLE, account)
            intent.putExtra(ExtraKey.SERIALIZABLE_2, mnemonic)
            context.startActivity(intent)
        }
    }
}