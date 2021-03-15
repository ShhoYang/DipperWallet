package com.highstreet.wallet.ui.activity

import android.view.View
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityAddTokenBinding
import com.highstreet.wallet.utils.ViewUtils
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
class AddTokenActivity : BaseActivity<ActivityAddTokenBinding, PlaceholderViewModel>(),
    View.OnFocusChangeListener {


    override fun initView() {

        viewBinding {
            etAddress.onFocusChangeListener = this@AddTokenActivity
            etName.onFocusChangeListener = this@AddTokenActivity
            etDecimalPlaces.onFocusChangeListener = this@AddTokenActivity

            RxView.click(btnNext) {

            }
        }
    }

    override fun initData() {
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewBinding {
            when (v) {
                etAddress -> ViewUtils.updateLineStyle(addressLine.line, hasFocus)
                etName -> ViewUtils.updateLineStyle(nameLine.line, hasFocus)
                etDecimalPlaces -> ViewUtils.updateLineStyle(decimalPlacesLine.line, hasFocus)
            }
        }
    }
}