package com.highstreet.wallet.ui.fragment

import android.os.Bundle
import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.ui.activity.ValidatorDetailActivity
import com.highstreet.wallet.ui.adapter.ValidatorAdapter
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.constant.ValidatorType
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.ValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorFragment : BaseListFragment<Validator, ValidatorVM>() {

    override fun createAdapter() = ValidatorAdapter()

    override fun initData() {
        viewModel.filterType = arguments?.getInt(ExtraKey.INT, ValidatorType.ALL)
                ?: ValidatorType.ALL
        super.initData()
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        context?.apply { ValidatorDetailActivity.start(this, item) }
    }

    companion object {
        fun instance(type: Int): ValidatorFragment {
            val fragment = ValidatorFragment()
            val bundle = Bundle()
            bundle.putInt(ExtraKey.INT, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}