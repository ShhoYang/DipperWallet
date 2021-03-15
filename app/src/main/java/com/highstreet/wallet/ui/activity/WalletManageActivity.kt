package com.highstreet.wallet.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.hao.library.adapter.listener.ItemDragListener
import com.hao.library.adapter.listener.ItemTouchCallback
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.gone
import com.hao.library.extensions.init
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.cache.BalanceCache
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.databinding.ActivityWalletManageBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.WalletType
import com.highstreet.wallet.ui.adapter.BottomMenuDialogAdapter
import com.highstreet.wallet.ui.adapter.WalletManageLeftAdapter
import com.highstreet.wallet.ui.adapter.WalletManageRightAdapter
import com.highstreet.wallet.ui.vm.WalletManageVM
import com.highstreet.wallet.view.BottomMenuDialog
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class WalletManageActivity :
    BaseActivity<ActivityWalletManageBinding, WalletManageVM>(), View.OnClickListener {

    private var editable by Delegates.observable(false) { _, old, new ->
        if (new != old) {
            viewBinding {
                if (new) {
                    ivEdit.visible()
                    ivEdit.setOnClickListener(this@WalletManageActivity)
                } else {
                    ivEdit.gone()
                    ivEdit.setOnClickListener(null)
                }
            }
        }
    }

    private var isEdit by Delegates.observable(false) { _, old, new ->
        if (new != old) {
            viewBinding {
                if (new) {
                    ivEdit.setImageResource(R.drawable.ic_completed)
                } else {
                    ivEdit.setImageResource(R.drawable.ic_sort)
                }
            }
        }
    }

    private lateinit var itemTouchHelper: ItemTouchHelper

    @Inject
    lateinit var leftAdapter: WalletManageLeftAdapter

    @Inject
    lateinit var rightAdapter: WalletManageRightAdapter

    private var bottomMenuDialogAdapter: BottomMenuDialogAdapter? = null

    private var bottomMenuDialog: BottomMenuDialog? = null

    override fun initView() {
        setTitle(R.string.walletManage)
        leftAdapter.setOnItemClickListener(object : OnItemClickListener<WalletType> {
            override fun itemClicked(view: View, item: WalletType, position: Int) {
                leftAdapter.data.forEach {
                    it.selected = item.chain == it.chain
                }
                leftAdapter.notifyDataSetChanged()
                resetRightData(leftAdapter.data, position)
            }
        })

        itemTouchHelper = ItemTouchHelper(ItemTouchCallback(object : ItemDragListener {
            override fun dragged(fromPosition: Int, toPosition: Int) {
                if (true == vm?.sort(rightAdapter.data, fromPosition, toPosition)) {
                    rightAdapter.notifyItemMoved(fromPosition, toPosition)
                }
            }
        }))
        rightAdapter.itemTouchHelper = itemTouchHelper
        rightAdapter.setOnItemClickListener(object : OnItemClickListener<Account> {
            override fun itemClicked(view: View, item: Account, position: Int) {
                toA(WalletDetailActivity::class.java)
                if (item.address == "") {
                    selectCreateOrImport(item.chain)
                } else {
                    WalletDetailActivity.start(this@WalletManageActivity, item)
                }
            }
        })

        viewBinding {
            rvLeft.init(leftAdapter)
            itemTouchHelper.attachToRecyclerView(rvRight)
            rvRight.init(rightAdapter)
        }
    }

    override fun initData() {
        Db.instance().accountDao().queryAllAsLiveData()
            .observe(this, Observer {
                processData(it ?: ArrayList())
            })
    }

    private fun processData(accounts: List<Account>) {
        val list = ArrayList<WalletType>()
        val mainList = ArrayList<Account>()
        val testList = ArrayList<Account>()
        accounts.forEach {
            it.balance = BalanceCache.instance().getBalance(it)
            if (it.isMain()) {
                mainList.add(it)
            } else if (it.isTest()) {
                testList.add(it)
            }
        }
        mainList.add(Account.empty(Chain.DIP_MAIN2))
        testList.add(Account.empty(Chain.DIP_TEST2))
        list.add(WalletType(Chain.ALL, 0, accounts, selected = true, isAll = true))
        list.add(WalletType(Chain.DIP_MAIN2, R.mipmap.dipper_hub, mainList))
        list.add(WalletType(Chain.DIP_TEST2, R.mipmap.dipper_test, testList))
        leftAdapter.resetData(list)
        resetRightData(list, 0)
    }

    private fun selectCreateOrImport(chain: String) {
        if (bottomMenuDialog == null) {
            bottomMenuDialogAdapter = BottomMenuDialogAdapter()
            bottomMenuDialogAdapter!!.setOnItemClickListener(object : OnItemClickListener<String> {
                override fun itemClicked(view: View, item: String, position: Int) {
                    when (position) {
                        0 -> CreateWalletActivity.start(this@WalletManageActivity, chain, true)
                        1 -> ImportWalletActivity.start(this@WalletManageActivity, chain, true)
                    }
                    bottomMenuDialog?.dismiss()
                }
            })
            val optionList = arrayListOf(
                getString(R.string.createWallet),
                getString(R.string.importWallet),
            )
            bottomMenuDialogAdapter!!.resetData(optionList)
            bottomMenuDialog = BottomMenuDialog(this).setAdapter(bottomMenuDialogAdapter!!)
        }

        bottomMenuDialog!!.show()
    }

    private fun resetRightData(leftList: ArrayList<WalletType>, currentLeftSelected: Int) {
        val right = leftList[currentLeftSelected]
        editable = right.isAll
        isEdit = false
        rightAdapter.edit = editable && isEdit
        rightAdapter.resetData(right.accounts)
    }

    private fun edit() {
        if (isEdit) {
            isEdit = false
            vm?.save(leftAdapter.data, rightAdapter.data)
        } else {
            isEdit = true
            rightAdapter.edit = editable && isEdit
            rightAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.ivEdit) {
            edit()
        }
    }
}