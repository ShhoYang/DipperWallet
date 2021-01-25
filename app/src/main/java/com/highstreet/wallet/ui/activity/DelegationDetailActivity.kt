package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.DelegationDetailVM
import kotlinx.android.synthetic.main.g_activity_delegation_detail.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class DelegationDetailActivity : BaseActivity(), View.OnClickListener {

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null
    private var reward: String = "0"

    private val viewModel by lazy {
        ViewModelProvider(this).get(DelegationDetailVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_delegation_detail

    override fun initView() {
        title = "委托详情"

        RxView.click(tvDetail, this)
        RxView.click(ivDetail, this)
        RxView.click(llRedelegate, this)
        RxView.click(llUndelegate, this)
        RxView.click(llReward, this)
        RxView.click(llDelegate, this)
    }

    override fun initData() {
        val isUndelegate = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        llRedelegate.visibility(!isUndelegate)
        llUndelegate.visibility(!isUndelegate)
        llReward.visibility(!isUndelegate)
        llDelegate.visibility(!isUndelegate)
        tvUnDelegateAmount.visibility(isUndelegate)
        viewModel.validatorLD.observe(this, Observer {
            validator = it
            validator?.apply {
                tvAvatar.text = getFirstLetterName()
                tvName.text = description?.moniker
                tvAddress.text = operator_address
                tvRate.text = getRate()
            }
        })
        viewModel.rewardLD.observe(this, Observer {
            reward = it ?: "0"
            tvReward.text = reward
        })

        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewModel.getValidator(validator_address)
            viewModel.getReward(validator_address)
            tvAmount.text = StringUtils.pdip2DIP(shares, false)
            tvReward.text = reward
            if (isUndelegate) {
                tvUnDelegateAmount.text = "${StringUtils.formatDecimal(shares)}解委托中\n（剩余${StringUtils.timeGap(completionTime)}）"
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            tvDetail, ivDetail -> {
                if (null != validator) {
                    ValidatorDetailActivity.start(this, validator!!)
                }
            }
            llRedelegate -> {
                if (null != delegationInfo) {
                    RedelegationActivity.start(this, delegationInfo!!)
                }
            }
            llUndelegate -> {
                if (null != delegationInfo) {
                    UndelegationActivity.start(this, delegationInfo!!)
                }
            }
            llReward -> {
                if (TextUtils.isEmpty(reward) || "0" == reward || reward.toDouble() == 0.0) {
                    toast("奖励为0")

                } else {
                    val validatorAddress = delegationInfo?.validator_address
                    val delegatorAddress = delegationInfo?.delegator_address
                    if (!TextUtils.isEmpty(validatorAddress) && !TextUtils.isEmpty(delegatorAddress)) {
                        ReceiveRewardActivity.start(this, validatorAddress!!, delegatorAddress!!, reward + "DIP")
                    }
                }
            }
            llDelegate -> {
                if (null != validator) {
                    DelegationActivity.start(this, validator!!)
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