package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.init
import com.hao.library.ui.BaseFragment
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.FragmentListBinding
import com.highstreet.wallet.model.Menu
import com.highstreet.wallet.ui.activity.DAppActivity
import com.highstreet.wallet.ui.adapter.DAppAdapter

/**
 * @author Yang Shihao
 */

@AndroidEntryPoint(injectViewModel = false)
class DAppFragment : BaseFragment<FragmentListBinding, PlaceholderViewModel>(),
    OnItemClickListener<Menu> {

    @Inject
    lateinit var adapter: DAppAdapter

    override fun initView() {
        val list = ArrayList<Menu>()
        list.add(
            Menu(
                icon = R.mipmap.ic_app_launcher,
                title = getString(R.string.dipsyn),
            )
        )
        list.add(
            Menu(
                icon = R.mipmap.ic_app_launcher,
                title = getString(R.string.dipdex),
            )
        )
        list.add(
            Menu(
                icon = R.mipmap.ic_app_launcher,
                title = getString(R.string.dipbank),
            )
        )
        adapter.setOnItemClickListener(this)
        vb?.baseRecyclerView?.init(adapter)
        adapter.resetData(list)
    }

    override fun initData() {

    }

    override fun itemClicked(view: View, item: Menu, position: Int) {
        context?.apply { DAppActivity.start(this, item.title, "https://uniswap.token.im/#/swap?locale=zh-CN&utm_source=imtoken") }
    }
}