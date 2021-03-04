package com.highstreet.wallet.ui.fragment

import android.os.Bundle
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.FragmentBaseListBinding
import com.hao.library.ui.BaseListFragment
import com.hao.library.ui.UIParams
import com.highstreet.wallet.ui.adapter.DelegationTransactionRecordAdapter
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.ui.vm.DelegationTransactionRecordVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

@AndroidEntryPoint
class DelegationTransactionRecordFragment :
    BaseListFragment<FragmentBaseListBinding, Tx, DelegationTransactionRecordVM, DelegationTransactionRecordAdapter>() {

    override fun prepare(uiParams: UIParams, bundle: Bundle?) {
        super.prepare(uiParams, bundle)
        vm?.type = bundle?.getString(ExtraKey.STRING) ?: TYPE_BOND
    }

    companion object {
        const val TYPE_BOND = "bond"
        const val TYPE_UN_BOND = "unbond"
        const val TYPE_REDELEGATE = "redelegate"

        fun instance(type: String): DelegationTransactionRecordFragment {
            val fragment = DelegationTransactionRecordFragment()
            val bundle = Bundle()
            bundle.putString(ExtraKey.STRING, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}