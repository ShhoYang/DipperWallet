package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.init
import com.hao.library.ui.BaseFragment
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.FragmentBrowserBinding
import com.highstreet.wallet.model.Menu
import com.highstreet.wallet.ui.activity.DAppActivity
import com.highstreet.wallet.ui.adapter.DAppAdapter

/**
 * @author Yang Shihao
 */

@AndroidEntryPoint(injectViewModel = false)
class BrowserFragment : BaseFragment<FragmentBrowserBinding, PlaceholderViewModel>(),
    OnItemClickListener<Menu> {

    @Inject
    lateinit var adapter: DAppAdapter

    override fun initView() {
        val list = ArrayList<Menu>()
        list.add(
            Menu(
                title = getString(R.string.dipsyn),
                icon = R.mipmap.ic_launcher,
                data = ""
            )
        )
        list.add(
            Menu(
                title = getString(R.string.dipdex),
                icon = R.mipmap.ic_launcher,
                data = ""
            )
        )
        list.add(
            Menu(
                title = getString(R.string.dipbank),
                icon = R.mipmap.ic_launcher,
                data = ""
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