package com.highstreet.wallet.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.init
import com.hao.library.extensions.visibility
import com.hao.library.ui.BaseFragment
import com.hao.library.utils.CoroutineUtils
import com.hao.library.view.dialog.ConfirmDialog
import com.hao.library.view.dialog.ConfirmDialogListener
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.databinding.FragmentMeBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.fingerprint.DialogParams
import com.highstreet.wallet.fingerprint.FingerprintM
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.fingerprint.IFingerprint
import com.highstreet.wallet.fingerprint.listener.FingerprintCallback
import com.highstreet.wallet.model.Menu
import com.highstreet.wallet.ui.activity.AboutActivity
import com.highstreet.wallet.ui.activity.WalletManageActivity
import com.highstreet.wallet.ui.adapter.MenuAdapter
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint(injectViewModel = false)
class MeFragment : BaseFragment<FragmentMeBinding, PlaceholderViewModel>(),
    OnItemClickListener<Menu>, FingerprintCallback,
    ConfirmDialogListener {

    @Inject
    lateinit var menuAdapter: MenuAdapter

    private var fingerprintM: IFingerprint? = null

    private var setFingerprint = false

    override fun initView() {
        val isFingerprint = FingerprintUtils.isHardwareDetected(context)

        val menuList = ArrayList<Menu>()
        if (isFingerprint) {
            menuList.add(0, Menu.wide())
        }
        menuList.add(
            Menu(
                title = getString(R.string.walletManager),
                icon = R.mipmap.my_wallet,
                action = WalletManageActivity::class.java
            )
        )
        menuList.add(Menu.wide())
        menuList.add(
            Menu(
                title = getString(R.string.about),
                icon = R.mipmap.my_friend,
                action = AboutActivity::class.java
            )
        )

        menuAdapter.setOnItemClickListener(this@MeFragment)
        viewBinding {
            clFingerprint.visibility(isFingerprint)
            baseRecyclerView.init(menuAdapter)
            RxView.click(ivSwitch) {
                if (setFingerprint) {
                    activity?.let {
                        ConfirmDialog.Builder(it)
                            .setMessage(getString(R.string.cancelFingerprintVerification))
                            .setListener(this@MeFragment)
                            .build()
                            .show()
                    }
                } else if (FingerprintUtils.hasEnrolledFingerprints(context)) {
                    getFingerprint()?.authenticate()
                } else {
                    toast(R.string.noFingerprint)
                }
            }
        }

        menuAdapter.resetData(menuList)
    }

    override fun initData() {
        Db.instance().passwordDao().queryByIdAsLiveData(Constant.PASSWORD_DEFAULT_ID)
            .observe(this, Observer {
                setFingerprint = it?.fingerprint ?: false
                vb?.ivSwitch?.setImageResource(
                    if (setFingerprint) {
                        R.mipmap.icon_switch_open
                    } else {
                        R.mipmap.icon_switch_close
                    }
                )
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

    override fun itemClicked(view: View, item: Menu, position: Int) {
        if (item.action == null) {
        } else {
            this@MeFragment.toA(item.action)
        }
    }

    /**
     * 指纹监听
     */
    override fun onFingerprintAuthenticateSucceed() {
        setFingerprint = !setFingerprint
        AccountManager.instance().password?.let {
            CoroutineUtils.io {
                it.fingerprint = setFingerprint
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