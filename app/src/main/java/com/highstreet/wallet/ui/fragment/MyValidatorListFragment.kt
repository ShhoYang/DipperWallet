package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.FragmentBaseListBinding
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.activity.ValidatorDetailActivity
import com.highstreet.wallet.ui.adapter.MyValidatorAdapter
import com.highstreet.wallet.ui.vm.MyValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class MyValidatorListFragment :
    BaseListFragment<FragmentBaseListBinding, Validator, MyValidatorVM, MyValidatorAdapter>() {

    override fun itemClicked(view: View, item: Validator, position: Int) {
        act {
            ValidatorDetailActivity.start(it, item, true)
        }
    }
}