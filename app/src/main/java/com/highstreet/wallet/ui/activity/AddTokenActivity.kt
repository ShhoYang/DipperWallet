package com.highstreet.wallet.ui.activity

import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.databinding.ActivityAddTokenBinding
import com.highstreet.wallet.extensions.focusListener

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
class AddTokenActivity : BaseActivity<ActivityAddTokenBinding, PlaceholderViewModel>() {

    override fun initView() {
        viewBinding {
            etAddress.focusListener(addressLine.line)
            etName.focusListener(nameLine.line)
            etDecimalPlaces.focusListener(decimalPlacesLine.line)
        }
    }

    override fun initData() {
    }
}