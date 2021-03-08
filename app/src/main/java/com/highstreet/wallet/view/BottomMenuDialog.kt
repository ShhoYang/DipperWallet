package com.highstreet.wallet.view

import android.app.Activity
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hao.library.view.dialog.BaseDialog
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.DialogBottomMenuBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class BottomMenuDialog(activity: Activity) :
    BaseDialog<DialogBottomMenuBinding>(activity = activity) {

    override fun getVB() = DialogBottomMenuBinding.inflate(layoutInflater)

    override fun setWindowParams(window: Window) {
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.BOTTOM
        window.attributes = attributes
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun initView() {
        viewBinding {
            tvCancel.setOnClickListener {
                dismiss()
            }
            rv.layoutManager = LinearLayoutManager(activity)
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>): BottomMenuDialog {
        viewBinding {
            rv.adapter = adapter
        }

        return this
    }
}