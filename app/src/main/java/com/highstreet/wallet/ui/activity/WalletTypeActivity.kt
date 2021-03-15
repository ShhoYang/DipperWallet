package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityListBinding
import com.highstreet.wallet.model.WalletType
import com.highstreet.wallet.ui.adapter.WalletTypeAdapter

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class WalletTypeActivity :
    BaseNormalListActivity<ActivityListBinding, WalletType, PlaceholderViewModel, WalletTypeAdapter>() {

    override fun initView() {
        setTitle(R.string.walletType)
        super.initView()
    }

    override fun initData() {
        val list = ArrayList<WalletType>()
        list.add(WalletType(Chain.DIP_MAIN2, R.mipmap.dipper_hub))
        list.add(WalletType(Chain.DIP_TEST2, R.mipmap.dipper_test))
        adapter.resetData(list)
    }

    override fun itemClicked(view: View, item: WalletType, position: Int) {
        val toDoType =
            intent.getIntExtra(ExtraKey.INT, TO_DO_TYPE_CREATE)
        val isAdd = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        if (TO_DO_TYPE_CREATE == toDoType) {
            CreateWalletActivity.start(this@WalletTypeActivity, item.chain.chainName, isAdd)
            finish()
        } else {
            ImportWalletActivity.start(this@WalletTypeActivity, item.chain.chainName, isAdd)
            finish()
        }
    }

    companion object {

        // 创建
        const val TO_DO_TYPE_CREATE = 1

        // 导入
        const val TO_DO_TYPE_IMPORT = 2

        fun start(context: Context, toDoType: Int, isAdd: Boolean = false) {
            val intent = Intent(context, WalletTypeActivity::class.java)
            intent.putExtra(ExtraKey.INT, toDoType)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}