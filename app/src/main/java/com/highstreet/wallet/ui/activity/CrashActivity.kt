package com.highstreet.wallet.ui.activity

import android.content.pm.PackageManager
import android.os.Build
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityCarshBinding
import com.highstreet.wallet.view.listener.RxView
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author Yang Shihao
 * @Date 2019-07-20
 */
@AndroidEntryPoint(injectViewModel = false)
class CrashActivity : BaseActivity<ActivityCarshBinding, PlaceholderViewModel>() {

    override fun initView() {
        toolbarLayout {
            setTitleText("SORRY")
            showBack(false)
        }

        var error = CustomActivityOnCrash.getStackTraceFromIntent(intent)
        val rxPermissions = RxPermissions(this)
        if (rxPermissions.isGranted(android.Manifest.permission.READ_PHONE_STATE)) {
            error = getDeviceInfo() + error
        }
        viewBinding {
            tvDetails.text = error
            RxView.click(btnRestart) {
                val config = CustomActivityOnCrash.getConfigFromIntent(intent)
                if (config != null) {
                    CustomActivityOnCrash.restartApplication(this@CrashActivity, config)
                }
            }
        }
    }

    override fun initData() {
    }

    private fun getDeviceInfo(): String {
        val sb = StringBuffer("")
        try {
            val packageManager = packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

            if (packageInfo != null) {
                val versionName =
                    if (packageInfo.versionName == null) "null" else packageInfo.versionName
                sb.append("versionName = ").append(versionName).append("\n")
                    .append("versionCode = ").append(packageInfo.versionCode).append("\n")
            }
            val fields = Build::class.java.declaredFields
            for (field in fields) {
                field.isAccessible = true
                sb.append(field.name).append(" = ")
                    .append(field.get(null)?.toString() ?: "")
                    .append("\n")
            }
            sb.append("\n\n")
            return sb.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            return sb.toString()
        } catch (e: IllegalAccessException) {
            return sb.toString()
        }
    }
}