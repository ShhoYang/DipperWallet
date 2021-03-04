package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.visibility
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
        viewBinding {
            RxView.click(tvDetail, this@DelegationDetailActivity)
            RxView.click(ivDetail, this@DelegationDetailActivity)
            RxView.click(llRedelegate, this@DelegationDetailActivity)
            RxView.click(llUndelegate, this@DelegationDetailActivity)
            RxView.click(llReward, this@DelegationDetailActivity)
            RxView.click(llDelegate, this@DelegationDetailActivity)
        }
    }

    override fun initData() {
        val isUndelegate = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        viewBinding {
            llRedelegate.visibility(!isUndelegate)
            llUndelegate.visibility(!isUndelegate)
            llReward.visibility(!isUndelegate)
            llDelegate.visibility(!isUndelegate)
            tvUnDelegateAmount.visibility(isUndelegate)
        }
        viewModel {
            validatorLD.observe(this@DelegationDetailActivity, Observer {
                validator = it
                validator?.apply {
                    viewBinding {
                        tvAvatar.text = getFirstLetterName()
                        tvName.text = description?.moniker
                        tvAddress.text = operator_address
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
                tvDetail, ivDetail -> {
                    if (null != validator) {
                        ValidatorDetailActivity.start(this@DelegationDetailActivity, validator!!)
                    }
                }
                llRedelegate -> {
                    if (null != delegationInfo) {
                        RedelegationActivity.start(this@DelegationDetailActivity, delegationInfo!!)
                    }
                }
                llUndelegate -> {
                    if (null != delegationInfo) {
                        UndelegationActivity.start(this@DelegationDetailActivity, delegationInfo!!)
                    }
                }
                llReward -> {
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
                llDelegate -> {
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