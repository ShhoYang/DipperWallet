package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.FinalTallyResult
import com.highstreet.wallet.model.res.Proposal
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

class ProposalDetailVM : BaseViewModel() {

    val proposalLD = MutableLiveData<Proposal>()
    val rateLD = MutableLiveData<FinalTallyResult?>()
    val opinionLD = MutableLiveData<String>()
    val voteLD = MutableLiveData<Pair<Boolean, String>>()

    /**
     * 详情
     */
    private fun proposalDetail(proposalId: String) {
        ApiService.getDipApi().proposalDetail(proposalId).subscribeBy({
            proposalLD.value = it
        }, {
        }, false).add()
    }

    /**
     * 投票比例
     */
    fun votingRate(proposalId: String) {
        ApiService.getDipApi().votingRate(proposalId).subscribeBy({
            rateLD.value = it
        }, {
        }, false).add()
    }

    /**
     * 我的意见
     */
    fun proposalOpinion(proposalId: String) {
        ApiService.getDipApi().proposalOpinion(proposalId, AccountManager.instance().address)
            .subscribeBy({
                opinionLD.value = it?.option
            }, {
            }, false).add()
    }

    /**
     * 投票
     */
    fun vote(proposalId: String, opinion: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (null == it) {
                voteLD.value = Pair(false, "")
            } else {
                generateParams(it, proposalId, opinion)
            }
        }, {
            voteLD.value = Pair(false, "")
        }).add()
    }

    private fun generateParams(accountInfo: AccountInfo, proposalId: String, opinion: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.voteMsg(
            account.address,
            proposalId,
            opinion,
            account.chain
        )
        doVote(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                "",
                deterministicKey
            ), proposalId
        )
    }

    private fun doVote(reqBroadCast: RequestBroadCast, proposalId: String) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy2({
            if (true == it?.success()) {
                proposalDetail(proposalId)
                votingRate(proposalId)
                proposalOpinion(proposalId)
                voteLD.value = Pair(true, "")
            } else {
                voteLD.value = Pair(false, "")
            }

        }, {
            voteLD.value = Pair(false, it.second)
        }).add()
    }
}