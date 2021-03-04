package com.highstreet.wallet.ui.fragment

import android.os.Bundle
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.FragmentBaseListBinding
import com.hao.library.ui.BaseListFragment
import com.hao.library.ui.UIParams
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.ui.adapter.TransactionRecordAdapter
import com.highstreet.wallet.ui.vm.TransactionRecordVM


/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */

@AndroidEntryPoint
class TransactionRecordFragment :
    BaseListFragment<FragmentBaseListBinding, Tx, TransactionRecordVM, TransactionRecordAdapter>() {

    override fun prepare(uiParams: UIParams, bundle: Bundle?) {
        super.prepare(uiParams, bundle)
        vm?.isIn = bundle?.getBoolean(ExtraKey.BOOLEAN, true) ?: true
    }

    companion object {
        fun instance(isIn: Boolean): TransactionRecordFragment {
            val fragment = TransactionRecordFragment()
            val bundle = Bundle()
            bundle.putBoolean(ExtraKey.BOOLEAN, isIn)
            fragment.arguments = bundle
            return fragment
        }
    }
}