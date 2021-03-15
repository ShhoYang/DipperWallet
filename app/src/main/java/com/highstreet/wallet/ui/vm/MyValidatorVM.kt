package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Rewards
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class MyValidatorVM : BaseListViewModel<Validator>() {

    private var validatorList: ArrayList<Validator>? = null
    private var deledationList: ArrayList<DelegationInfo>? = null
    private var unbondingDelegationList: ArrayList<DelegationInfo>? = null
    private var rewards: Rewards? = null
    private var response: ((ArrayList<Validator>?) -> Unit)? = null

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {
        val address = AccountManager.instance().address
        ApiService.getApi()
            .validatorsByAddress(address, 1, pageSize()).subscribeBy({
                if (it == null || it.isEmpty()) {
                    onResponse(it)
                } else {
                    validatorList = it
                    response = onResponse
                    getDelegationAmount(address)
                }
            }, {
                onResponse(null)
            }).add()
    }

    private fun getDelegationAmount(address: String) {
        ApiService.getApi().delegations(address, 1, pageSize())
            .subscribeBy({
                deledationList = it
                getUnbondingDelegationAmount(address)
            }, {
                getUnbondingDelegationAmount(address)
            }).add()
    }

    private fun handleDelegations(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        var amount = 0L

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
        }

    }

    private fun getUnbondingDelegationAmount(address: String) {
        ApiService.getApi().unbondingDelegations(address, 1, pageSize())
            .subscribeBy({
                unbondingDelegationList = it
                getRewards(address)
            }, {
                getRewards(address)
            }).add()
    }

    private fun handleUnbondingDelegations(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        var amount = 0L

        list?.forEach {
            it?.entries?.forEach { e ->
                amount += e?.balance?.toDouble()?.toLong() ?: 0
            }
        }
    }

    private fun getRewards(address: String) {
        ApiService.getApi().rewards(address).subscribeBy({
            rewards = it
            processData()
        }, {
            processData()
        }).add()
    }

    private fun processData() {
        validatorList?.forEach { validator ->
            validator.delegationInfo =
                deledationList?.find { validator.operator_address == it.validator_address }

            validator.unbondingDelegationInfo =
                unbondingDelegationList?.find { validator.operator_address == it.validator_address }

            validator.reward =
                rewards?.rewards?.find { validator.operator_address == it.validator_address }
        }
        response?.let { it(validatorList) }
    }
}