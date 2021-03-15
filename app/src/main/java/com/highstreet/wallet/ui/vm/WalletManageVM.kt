package com.highstreet.wallet.ui.vm

import com.hao.library.utils.CoroutineUtils
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.WalletType

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
class WalletManageVM : BaseViewModel() {

    fun sort(data: ArrayList<Account>, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition !in data.indices
            || toPosition !in data.indices
        ) {
            return false
        }

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                swap(data, i, i - 1)
            }
        }

        return true
    }

    private fun swap(data: ArrayList<Account>, index1: Int, index2: Int) {
        val data1 = data[index1]
        val data2 = data[index2]
        val temp = data1.sort
        data1.sort = data2.sort
        data2.sort = temp
        data[index1] = data2
        data[index2] = data1
    }

    /**
     * 排序后保存
     */
    fun save(leftList: List<WalletType>, sortedList: List<Account>) {
        val originList = leftList.find { it.isAll } ?: return
        if (!listIsEqual(originList.accounts, sortedList)) {
            sortedList.forEachIndexed { index, account ->
                account.sort = index
            }
            CoroutineUtils.io {
                Db.instance().accountDao().updateAll(sortedList)
            }
        }
    }

    private fun listIsEqual(list1: List<Account>, list2: List<Account>): Boolean {
        if (list1.size != list2.size) {
            return false
        }

        for (i in list1.indices) {
            if (list1[i].address != list2[i].address) {
                return false
            }
        }

        return true
    }
}
