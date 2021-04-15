package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityScanBinding
import com.king.zxing.CaptureHelper
import com.king.zxing.OnCaptureCallback

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint(injectViewModel = false)
class ScanActivity : BaseActivity<ActivityScanBinding, PlaceholderViewModel>(), OnCaptureCallback {

    private var captureHelper: CaptureHelper? = null

    override fun initView() {
        toolbarLayout {
            setTitleText(R.string.scan)
            setIconColor(Color.WHITE)
        }
    }

    override fun initData() {
        viewBinding {
            captureHelper = CaptureHelper(this@ScanActivity, surfaceView, viewfinderView, null)
        }
        captureHelper?.apply {
            setOnCaptureCallback(this@ScanActivity)
            onCreate()
            vibrate(false)
            fullScreenScan(true)
            supportVerticalCode(false)
            supportLuminanceInvert(false)
            continuousScan(false)
        }
    }


    override fun onResume() {
        super.onResume()
        captureHelper?.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureHelper?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureHelper?.onDestroy()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        captureHelper?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onResultCallback(result: String): Boolean {
        val intent = Intent()
        intent.putExtra(ExtraKey.STRING, result)
        setResult(RESULT_OK, intent)
        finish()
        return true
    }

    companion object {
        const val REQUEST_CODE_SCAN = 102
        fun start(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, ScanActivity::class.java),
                REQUEST_CODE_SCAN
            )
        }
    }
}