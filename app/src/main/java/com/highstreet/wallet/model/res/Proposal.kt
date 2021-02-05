package com.highstreet.wallet.model.res

import android.content.Context
import com.highstreet.lib.adapter.BaseItem
import com.highstreet.wallet.R
import com.highstreet.wallet.model.req.Coin
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class Proposal(
        val content: Content?,
        val deposit_end_time: String?,
        val final_tally_result: FinalTallyResult?,
        val id: String,
        val proposal_status: String,
        val proposer: String?,
        val submit_time: String?,
        val total_deposit: List<Coin>?,
        val voting_end_time: String?,
        val voting_start_time: String?
) : BaseItem, Serializable {

    fun getStatus(context: Context): String {
        return when (proposal_status) {
            "Nil" -> ""
            "DepositPeriod" -> context.getString(R.string.proposalDepositPeriod)
            "VotingPeriod" -> context.getString(R.string.proposalVotingPeriod)
            "Passed" -> context.getString(R.string.proposalPassed)
            "Rejected" -> context.getString(R.string.proposalRejected)
            "Failed" -> context.getString(R.string.proposalFailed)
            else -> ""
        }
    }

    fun isPassed(): Boolean {
        return "Passed" == proposal_status
    }

    fun isVotingPeriod(): Boolean {
        return "VotingPeriod" == proposal_status
    }

    override fun uniqueKey(): String {
        return "$id"
    }
}

data class Content(
        val type: String?,
        val value: ProposalValue?
) : Serializable

data class FinalTallyResult(
        val abstain: String?,
        val no: String?,
        val no_with_veto: String?,
        val yes: String?
) : Serializable

data class ProposalValue(
        val changes: List<Change>?,
        val description: String?,
        val title: String?
) : Serializable

data class Change(
        val key: String?,
        val subspace: String?,
        val value: String?
) : Serializable