package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.hao.library.AppManager
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityInitWalletBinding
import com.highstreet.wallet.model.WalletType
import com.highstreet.wallet.ui.adapter.CenterMenuDialogAdapter2
import com.highstreet.wallet.view.CenterMenuDialog

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class InitWalletActivity : BaseActivity<ActivityInitWalletBinding, PlaceholderViewModel>() {

    private var centerMenuDialogAdapter: CenterMenuDialogAdapter2<WalletType>? = null

    private var centerMenuDialog: CenterMenuDialog? = null

    private var action = -1

    override fun initView() {
        toolbarLayout {
            showBack(false)
        }
        viewBinding {
            RxView.click(btnCreate, TO_CREATE, showChain)
            RxView.click(btnImport, TO_IMPORT, showChain)
        }
    }

    override fun initData() {
        if (AccountManager.instance().accounts.isEmpty()) {
            AppManager.instance().finishAllActivityExceptAppoint(this)
        }
    }

    private val showChain: (Int) -> Unit = {
        action = it
        if (centerMenuDialog == null) {
            centerMenuDialogAdapter = CenterMenuDialogAdapter2()
            centerMenuDialogAdapter!!.setOnItemClickListener(object :
                OnItemClickListener<WalletType> {
                override fun itemClicked(view: View, item: WalletType, position: Int) {
                    toCreateOrImport(item.chain)
                    centerMenuDialog?.dismiss()
                }
            })

            val optionList = ArrayList<WalletType>()
            optionList.add(WalletType(Chain.DIP_MAIN, R.mipmap.dipper_hub))
            optionList.add(WalletType(Chain.DIP_TEST, R.mipmap.dipper_test))
            centerMenuDialogAdapter!!.resetData(optionList)
            centerMenuDialog = CenterMenuDialog(this).setAdapter(centerMenuDialogAdapter!!)
        }

        centerMenuDialog!!.show()
    }

    private fun toCreateOrImport(chain: Chain) {
        when (action) {
            TO_CREATE -> CreateWalletActivity.start(this, chain.chainName, false)
            TO_IMPORT -> ImportWalletActivity.start(this, chain.chainName, false)
        }
    }

    companion object {

        // 创建
        const val TO_CREATE = 1

        // 导入
        const val TO_IMPORT = 2

        /**
         * @param isAdd true添加 false新建
         */
        fun start(context: Context, isAdd: Boolean = false) {
            val intent = Intent(context, InitWalletActivity::class.java)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}