package com.highstreet.wallet.utils

import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.MsgType
import com.highstreet.wallet.crypto.Base64Utils
import com.highstreet.wallet.crypto.SHA
import com.highstreet.wallet.crypto.HexUtils
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.model.req.*
import org.bitcoinj.crypto.DeterministicKey

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
object MsgGeneratorUtils {

    fun sendMsg(
        from_address: String,
        to_address: String,
        amount: ArrayList<Coin>,
        chain: String
    ): Msg {
        val msgValue = MsgValue()
        msgValue.from_address = from_address
        msgValue.to_address = to_address
        msgValue.amount = amount
        return Msg(MsgType.SEND, msgValue)
    }

    fun delegateMsg(
        delegator_address: String,
        validator_address: String,
        amount: Coin,
        chain: String
    ): Msg {
        val msgValue = MsgValue()
        msgValue.delegator_address = delegator_address
        msgValue.validator_address = validator_address
        msgValue.amount = amount
        return Msg(MsgType.DELEGATE, msgValue)
    }

    fun undelegateMsg(
        delegator_address: String,
        validator_address: String,
        amount: Coin,
        chain: String
    ): Msg {
        val msgValue = MsgValue()
        msgValue.delegator_address = delegator_address
        msgValue.validator_address = validator_address
        msgValue.amount = amount
        return Msg(MsgType.UNDELEGATE, msgValue)
    }

    fun redelegateMsg(
        delegator_address: String,
        validator_src_address: String,
        validator_dst_address: String,
        amount: Coin,
        chain: String
    ): Msg {
        val msgValue = MsgValue()
        msgValue.delegator_address = delegator_address
        msgValue.validator_src_address = validator_src_address
        msgValue.validator_dst_address = validator_dst_address
        msgValue.amount = amount
        return Msg(MsgType.REDELEGATE, msgValue)
    }

    fun voteMsg(voter: String, proposal_id: String, option: String, chain: String): Msg {
        val msgValue = MsgValue()
        msgValue.voter = voter
        msgValue.proposal_id = proposal_id
        msgValue.option = option
        return Msg(MsgType.VOTE, msgValue)
    }

    fun receiveRewardMsg(validator_address: String, delegator_address: String, chain: String): Msg {
        val msgValue = MsgValue()
        msgValue.validator_address = validator_address
        msgValue.delegator_address = delegator_address
        return Msg(MsgType.RECEIVE_REWARD, msgValue)
    }

    fun getBroadCast(
        account: Account,
        msg: Msg,
        fee: Fee,
        memo: String,
        key: DeterministicKey
    ): RequestBroadCast {

        val msgs = arrayListOf(msg)

        val signatureMsg = SignatureMsg(
            account.chain,
            account.accountNumber.toString(),
            account.sequenceNumber.toString(),
            msgs,
            fee,
            memo
        )

        val signatureTx = sign(key, signatureMsg.toByteArray())
        val publicKey = PublicKey(Constant.KEY_TYPE_PUBLIC, KeyUtils.getPubKeyValue(key))
        val signature = Signature(
            publicKey,
            signatureTx,
            account.accountNumber.toString(),
            account.sequenceNumber.toString()
        )

        val stdTxValue = StdTxValue(msgs, fee, arrayListOf(signature), memo)

        return RequestBroadCast("block", stdTxValue)
    }

    private fun sign(deterministicKey: DeterministicKey, data: ByteArray): String {
        val signature = deterministicKey.sign(SHA.createSha256Hash(data))
        val byteArray = ByteArray(64)
        System.arraycopy(HexUtils.integerToBytes(signature.r, 32), 0, byteArray, 0, 32)
        System.arraycopy(HexUtils.integerToBytes(signature.s, 32), 0, byteArray, 32, 32)
        return Base64Utils.encodeToString(byteArray).replace("\n", "")
    }
}