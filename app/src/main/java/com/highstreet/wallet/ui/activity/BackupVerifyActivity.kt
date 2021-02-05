package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.extensions.init
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.utils.CoroutineUtils
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.ui.adapter.MnemonicAdapter
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.db.Account
import kotlinx.android.synthetic.main.g_activity_backup_verify.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class BackupVerifyActivity : BaseActivity() {

    private var from = BackupActivity.FROM_CREATE
    private var account: Account? = null
    private var mnemonic: ArrayList<String>? = null

    private var mnemonicS = ""

    private val topList = ArrayList<String>()
    private val bottomList = ArrayList<String>()


    override fun getLayoutId() = R.layout.g_activity_backup_verify

    override fun initView() {
        setTitle(R.string.backupMnemonic)
        from = intent.getIntExtra(ExtraKey.INT, BackupActivity.FROM_CREATE)
        account = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Account?
        mnemonic = intent.getSerializableExtra(ExtraKey.SERIALIZABLE_2) as ArrayList<String>?
        if (account == null || mnemonic == null || mnemonic!!.isEmpty()) {
            finish()
            return
        }
        topList.clear()
        bottomList.clear()
        bottomList.addAll(mnemonic!!)
        mnemonicS = bottomList.joinToString()
        bottomList.shuffle()
        val topAdapter = MnemonicAdapter(topList)
        val bottomAdapter = MnemonicAdapter(bottomList)
        topAdapter.itemClickListener = object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                topList.removeAt(position)
                topAdapter.notifyDataSetChanged()

                bottomList.add(item)
                bottomList.shuffle()
                bottomAdapter.notifyDataSetChanged()

                checkCompleted()
            }
        }
        bottomAdapter.itemClickListener = object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                bottomList.removeAt(position)
                bottomList.shuffle()
                bottomAdapter.notifyDataSetChanged()

                topList.add(item)
                topAdapter.notifyDataSetChanged()
                checkCompleted()
            }
        }
        rvTop.init(topAdapter, 4)
        rvBottom.init(bottomAdapter, 4)

        RxView.click(btnComfirm) {
            verify()
        }
    }

    private fun checkCompleted() {
        btnComfirm.isEnabled = topList.size == Constant.MNEMONIC_SIZE
    }

    private fun verify() {
        if (mnemonicS == topList.joinToString()) {
            toast(R.string.succeed)
            CoroutineUtils.io2main({
                account!!.isBackup = true
                AccountManager.instance().update(account!!)
            }, {
                if (BackupActivity.FROM_CREATE == from) {
                    to(MainActivity::class.java, true)
                }
                finish()
            })
        } else {
            toast(R.string.invalidMnemonic)
        }
    }

    override fun onBackPressed() {
        if (BackupActivity.FROM_CREATE == from) {
            to(MainActivity::class.java, true)
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