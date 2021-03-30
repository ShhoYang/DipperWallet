package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityDelegationDetailBinding
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.DelegationDetailVM
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
@AndroidEntryPoint
class DelegationDetailActivity :
    BaseActivity<ActivityDelegationDetailBinding, DelegationDetailVM>() {

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null
    private var reward: String = "0"

    override fun initView() {
        setTitle(R.string.dda_delegationDetail)
    }

    override fun initData() {
        val isUndelegate = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        viewBinding {
            if (isUndelegate) {
                llBtn.gone()
                tvUnbondingDelegationAmount.visible()
            } else {
                llBtn.visible()
                tvUnbondingDelegationAmount.gone()
                RxView.click(btnRedelegate, toRedelegate)
                RxView.click(btnUndelegate, toUndelegate)
                RxView.click(btnRedeemReward, toRedeemReward)
                RxView.click(btnDelegate, toDelegate)
            }
        }
        viewModel {
            validatorLD.observe(this@DelegationDetailActivity) {
                validator = it
                validator?.apply {
                    viewBinding {
                        tvName.text = description?.moniker
                        tvShares.text = getDelegatorShares()
                        tvSelfShares.text = getSelfDelegation()
                        tvRate.text = getRate()
                    }
                }
            }
            rewardLD.observe(this@DelegationDetailActivity) {
                reward = it ?: "0"
                vb!!.tvReward.text = reward
            }
        }

        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewModel {
                getValidator(validator_address)
                getReward(validator_address)
            }
            viewBinding {
                tvAmount.text = getDelegationAmount()
                tvReward.text = reward
                if (isUndelegate) {
                    tvUnbondingDelegationAmount.text =
                        "${
                            getDelegationAmount(true)
                        } ${getString(R.string.dda_unbond)}\n（${
                            getString(R.string.dda_timeRemaining)
                        } ${StringUtils.timeGap(this@DelegationDetailActivity, completionTime)}）"
                }
            }
        }
    }

    private val toRedelegate = {
        if (null != delegationInfo) {
            RedelegateActivity.start(this, delegationInfo!!)
        }
    }

    private val toUndelegate = {
        if (null != delegationInfo) {
            UndelegateActivity.start(this, delegationInfo!!)
        }
    }

    private val toDelegate = {
        if (null != validator) {
            DelegateActivity.start(this, validator!!)
        }
    }

    private val toRedeemReward = {
        if (TextUtils.isEmpty(reward) || "0" == reward || reward.toDouble() == 0.0) {
        } else {
            val validatorAddress = delegationInfo?.validator_address
            val delegatorAddress = delegationInfo?.delegator_address
            if (!TextUtils.isEmpty(validatorAddress) && !TextUtils.isEmpty(delegatorAddress)) {
                RedeemRewardActivity.start(
                    this,
                    validatorAddress!!,
                    delegatorAddress!!,
                    reward + "DIP"
                )
            }
        }
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo, isUndelegate: Boolean = false) {
            val intent = Intent(context, DelegationDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            intent.putExtra(ExtraKey.BOOLEAN, isUndelegate)
            context.startActivity(intent)
        }
    }
}