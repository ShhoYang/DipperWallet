package com.highstreet.lib.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.highstreet.lib.R
import com.highstreet.lib.adapter.BaseNormalAdapter
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.extensions.init
import com.highstreet.lib.view.RefreshLayout
import com.highstreet.lib.view.StateView

/**
 * @author Yang Shihao
 */
abstract class BaseSimpleListActivity<T> : BaseActivity(),
    OnItemClickListener<T> {

    private var refreshLayout: RefreshLayout? = null
    private var stateView: StateView? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseNormalAdapter<T>

    override fun getLayoutId() = R.layout.activity_base_list

    override fun initView() {
        refreshLayout = findViewById(R.id.baseRefreshLayout)
        recyclerView = findViewById(R.id.baseRecyclerView)!!
        stateView = findViewById(R.id.baseEmptyView)
        adapter = createAdapter()
        registerDataObserver()
        adapter.itemClickListener = this
        recyclerView.init(adapter)
        refreshLayout?.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun registerDataObserver() {
        if (null == stateView) {
            return
        }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (adapter.data.isEmpty()) {
                    stateView?.state = StateView.Status.NO_DATA
                } else {
                    stateView?.state = StateView.Status.DISMISS
                }
            }
        })
    }


    override fun itemClicked(view: View, item: T, position: Int) {

    }

    fun onRefresh() {

    }

    abstract fun createAdapter(): BaseNormalAdapter<T>
}