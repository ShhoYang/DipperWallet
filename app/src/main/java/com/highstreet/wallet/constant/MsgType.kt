package com.highstreet.wallet.constant

/**
 * @author Yang Shihao
 * @Date 1/21/21
 *
 * 消息类型
 */
object MsgType {
    
    const val SEND = "dip/MsgSend"
    const val DELEGATE = "dip/MsgDelegate"
    const val UNDELEGATE = "dip/MsgUndelegate"
    const val REDELEGATE = "dip/MsgBeginRedelegate"
    const val VOTE = "dip/MsgVote"
    const val RECEIVE_REWARD = "dip/MsgWithdrawDelegationReward"
    const val CONTRACT = "dip/MsgContract"
}