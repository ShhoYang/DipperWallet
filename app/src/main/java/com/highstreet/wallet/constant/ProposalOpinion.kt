package com.highstreet.wallet.constant

import android.content.Context
import com.highstreet.wallet.R

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 *
 * 投票类型
 */

object ProposalOpinion {
    const val YES = "Yes"
    const val NO = "No"
    const val NO_WITH_VETO = "NoWithVeto"
    const val ABSTAIN = "Abstain"


    fun getOpinion(context: Context, s: String?): String {
        return when (s) {
            YES -> context.getString(R.string.yes)
            NO -> context.getString(R.string.no)
            NO_WITH_VETO -> context.getString(R.string.noWithVote)
            ABSTAIN -> context.getString(R.string.abstain)
            else -> ""
        }
    }
}