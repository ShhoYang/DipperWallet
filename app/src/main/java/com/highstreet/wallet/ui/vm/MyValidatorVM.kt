package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.BaseBean
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Rewards
import com.highstreet.wallet.model.res.Validator
import io.reactivex.Observable
import io.reactivex.functions.Function4

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class MyValidatorVM : BaseListViewModel<Validator>() {

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {
        val api = ApiService.getApi()
        val pageSize = pageSize()
        val address = AccountManager.instance().address

        val validatorsObservable = api.validatorsByAddress(address, 1, pageSize)
        val delegationsObservable = api.delegations(address, 1, pageSize)
        val unbondingDelegationsObservable = api.unbondingDelegations(address, 1, pageSize)
        val rewardsObservable = api.rewards(address)

        Observable.zip(
            validatorsObservable,
            delegationsObservable,
            unbondingDelegationsObservable,
            rewardsObservable,
            Function4<BaseBean<ArrayList<Validator>>, BaseBean<ArrayList<DelegationInfo>>, BaseBean<ArrayList<DelegationInfo>>, BaseBean<Rewards>, ArrayList<Validator>> { t1, t2, t3, t4 ->
                return@Function4 processData(t1, t2, t3, t4)
            })
            .subscribeBy2({
                onResponse(it)
            }, {
                onResponse(null)
            })
            .add()
    }

    private fun processData(
        t1: BaseBean<ArrayList<Validator>>,
        t2: BaseBean<ArrayList<DelegationInfo>>,
        t3: BaseBean<ArrayList<DelegationInfo>>,
        t4: BaseBean<Rewards>
    ): ArrayList<Validator> {
        val validatorList = t1.result
        val deledationList = t2.result
        val unbondingDelegationList = t3.result
        val rewards = t4.result?.rewards
        validatorList?.forEach { validator ->
            validator.delegationInfo =
                deledationList?.find { validator.operator_address == it.validator_address }

            validator.unbondingDelegationInfo =
                unbondingDelegationList?.find { validator.operator_address == it.validator_address }

            validator.reward =
                rewards?.find { validator.operator_address == it.validator_address }
        }
        return validatorList ?: arrayListOf()
    }
}