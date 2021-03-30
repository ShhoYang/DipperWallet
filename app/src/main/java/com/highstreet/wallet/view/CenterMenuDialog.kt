package com.highstreet.wallet.view

import android.app.Activity
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hao.library.utils.DisplayUtils
import com.hao.library.view.dialog.BaseDialog
import com.highstreet.wallet.databinding.DialogCenterMenuBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class CenterMenuDialog(activity: Activity) :
    BaseDialog<DialogCenterMenuBinding>(activity = activity) {

    override fun getVB() = DialogCenterMenuBinding.inflate(layoutInflater)

    override fun setWindowParams(window: Window) {
        val attributes = window.attributes
        val w = DisplayUtils.getScreenWidth(activity) / 5 * 3
        attributes.width = w
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun initView() {
        viewBinding {
            rv.layoutManager = LinearLayoutManager(activity)
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>): CenterMenuDialog {
        viewBinding {
            rv.adapter = adapter
        }
        return this
    }
}