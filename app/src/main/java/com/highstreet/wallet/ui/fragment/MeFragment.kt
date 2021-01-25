package com.highstreet.wallet.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.extensions.init
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.fingerprint.DialogParams
import com.highstreet.lib.fingerprint.listener.FingerprintCallback
import com.highstreet.lib.fingerprint.FingerprintM
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.fingerprint.IFingerprint
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.utils.CoroutineUtils
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.db.Password
import com.highstreet.wallet.ui.activity.AboutActivity
import com.highstreet.wallet.ui.activity.WalletManageActivity
import com.highstreet.wallet.ui.adapter.MenuAdapter
import com.highstreet.wallet.model.Menu
import kotlinx.android.synthetic.main.g_fragment_me.*


/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class MeFragment : BaseFragment(), FingerprintCallback, ConfirmDialogListener {

    private var fingerprintM: IFingerprint? = null

    private var setFingerprint = false

    override fun getLayoutId() = R.layout.g_fragment_me

    override fun initView() {
        val isFingerprint = FingerprintUtils.isHardwareDetected(context)
        clFingerprint.visibility(isFingerprint)

        val menuList = ArrayList<Menu>()
        if (isFingerprint) {
            menuList.add(0, Menu.wide())
        }
        menuList.add(Menu("钱包管理", R.mipmap.my_wallet, WalletManageActivity::class.java))
        menuList.add(Menu.wide())
        menuList.add(Menu("关于我们", R.mipmap.my_friend, AboutActivity::class.java))

        val menuAdapter = MenuAdapter(menuList)
        menuAdapter.itemClickListener = object : OnItemClickListener<Menu> {
            override fun itemClicked(view: View, item: Menu, position: Int) {
                if (item.cls == null) {

                } else {
                    this@MeFragment.to(item.cls)
                }
            }
        }
        baseRecyclerView.init(menuAdapter)
        RxView.click(ivSwitch) {
            if (setFingerprint) {
                activity?.let {
                    ConfirmDialog(it).setMsg("确定取消指纹验证？").setListener(this).show()
                }
            } else if (FingerprintUtils.hasEnrolledFingerprints(context)) {
                getFingerprint()?.authenticate()
            } else {
                toast("没有录入指纹，请先到系统设置录入指纹")
            }
        }
    }

    override fun initData() {
        Db.instance().passwordDao().queryByIdAsLiveData(Constant.PASSWORD_DEFAULT_ID)
            .observe(this, Observer {
                setFingerprint = it?.fingerprint ?: false
                if (setFingerprint) {
                    ivSwitch.setImageResource(R.mipmap.icon_switch_open)
                } else {
                    ivSwitch.setImageResource(R.mipmap.icon_switch_close)
                }
            })
    }

    private fun getFingerprint(): IFingerprint? {
        if (null == fingerprintM) {
            activity?.let {
                fingerprintM = FingerprintM()
                fingerprintM!!.init(
                    it,
                    true,
                    this,
                    DialogParams(useFingerprint = true, showUserPassword = false)
                )
            }
        }

        return fingerprintM
    }

    override fun onStop() {
        super.onStop()
        fingerprintM?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fingerprintM?.onDestroy()
    }

    /**
     * 指纹监听
     */
    override fun onFingerprintAuthenticateSucceed() {
        setFingerprint = !setFingerprint
        AccountManager.instance().password?.let {
            CoroutineUtils.io {
                AccountManager.instance().updatePassword(it)
            }
        }
    }

    override fun onFingerprintCancel() {

    }

    override fun usePassword(password: String): Boolean? {
        return true
    }

    override fun confirm() {
        getFingerprint()?.authenticate()
    }

    override fun cancel() {

    }
}