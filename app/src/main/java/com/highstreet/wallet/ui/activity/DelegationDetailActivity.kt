package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityDelegationDetailBinding
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.DelegationDetailVM
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
@AndroidEntryPoint
class DelegationDetailActivity :
    BaseActivity<ActivityDelegationDetailBinding, DelegationDetailVM>(), View.OnClickListener {

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null
    private var reward: String = "0"

    override fun initView() {
        setTitle(R.string.delegationDetail)
    }

    override fun initData() {
        val isUndelegate = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        viewBinding {
            if(isUndelegate){
                llBtn.gone()
                tvUnDelegateAmount.visible()
            }else {
                llBtn.visible()
                tvUnDelegateAmount.gone()
                RxView.click(btnRedelegate, this@DelegationDetailActivity)
                RxView.click(btnUndelegate, this@DelegationDetailActivity)
                RxView.click(btnReward, this@DelegationDetailActivity)
                RxView.click(btnDelegate, this@DelegationDetailActivity)
            }
        }
        viewModel {
            validatorLD.observe(this@DelegationDetailActivity, Observer {
                validator = it
                validator?.apply {
                    viewBinding {
                        tvName.text = description?.moniker
                        tvShares.text = StringUtils.pdip2DIP(delegator_shares)
                        tvSelfShares.text = StringUtils.pdip2DIP(self_delegation)
                        tvRate.text = getRate()
                    }
                }
            })
            rewardLD.observe(this@DelegationDetailActivity, Observer {
                reward = it ?: "0"
                vb!!.tvReward.text = reward
            })
        }

        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewModel {
                getValidator(validator_address)
                getReward(validator_address)
            }
            viewBinding {
                tvAmount.text = StringUtils.pdip2DIP(shares, false)
                tvReward.text = reward
                if (isUndelegate) {
                    tvUnDelegateAmount.text =
                        "${
                            StringUtils.pdip2DIP(
                                StringUtils.formatDecimal(shares),
                                true
                            )
                        } ${getString(R.string.unbond)}\n（${
                            getString(R.string.timeRemaining)
                        } ${StringUtils.timeGap(this@DelegationDetailActivity, completionTime)}）"
                }
            }
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                btnRedelegate -> {
                    if (null != delegationInfo) {
                        RedelegationActivity.start(this@DelegationDetailActivity, delegationInfo!!)
                    }
                }
                btnUndelegate -> {
                    if (null != delegationInfo) {
                        UndelegationActivity.start(this@DelegationDetailActivity, delegationInfo!!)
                    }
                }
                btnReward -> {
                    if (TextUtils.isEmpty(reward) || "0" == reward || reward.toDouble() == 0.0) {
                        toast(R.string.notEnough)
                    } else {
                        val validatorAddress = delegationInfo?.validator_address
                        val delegatorAddress = delegationInfo?.delegator_address
                        if (!TextUtils.isEmpty(validatorAddress) && !TextUtils.isEmpty(
                                delegatorAddress
                            )
                        ) {
                            ReceiveRewardActivity.start(
                                this@DelegationDetailActivity,
                                validatorAddress!!,
                                delegatorAddress!!,
                                reward + "DIP"
                            )
                        }
                    }
                }
                btnDelegate -> {
                    if (null != validator) {
                        DelegationActivity.start(this@DelegationDetailActivity, validator!!)
                    }
                }
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