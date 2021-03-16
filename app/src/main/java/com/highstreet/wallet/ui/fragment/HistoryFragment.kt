package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.FragmentBaseListBinding
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.ui.activity.TxDetailActivity
import com.highstreet.wallet.ui.adapter.HistoryAdapter
import com.highstreet.wallet.ui.vm.HistoryVM

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint
class HistoryFragment :
    BaseListFragment<FragmentBaseListBinding, Tx, HistoryVM, HistoryAdapter>() {

    override fun initData() {
        AccountManager.instance().balanceLiveData.observe(this) {
            vm?.refresh()
        }
        Db.instance().accountDao().queryFirstUserAsLiveData().observe(this) {
            vm?.refresh()
        }
        super.initData()
    }

    override fun itemClicked(view: View, item: Tx, position: Int) {
        super.itemClicked(view, item, position)
        act {
            TxDetailActivity.start(it, item)
        }
    }
}