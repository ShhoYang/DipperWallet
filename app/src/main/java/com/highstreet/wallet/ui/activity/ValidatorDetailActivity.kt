package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityValidatorDetailBinding
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint(injectViewModel = false)
class ValidatorDetailActivity :
    BaseActivity<ActivityValidatorDetailBinding, PlaceholderViewModel>(){

    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.vda_validatorDetail)
        viewBinding {
            if (intent.getBooleanExtra(ExtraKey.BOOLEAN, false)) {
                btnDelegate2.gone()
                llDelegation.visible()
            } else {
                btnDelegate2.visible()
                llDelegation.gone()
            }

            RxView.click(btnDelegate2, toDelegate)
            RxView.click(btnDelegate, toDelegate)
            RxView.click(btnUndelegate, toUndelegate)
            RxView.click(btnRedelegate, toRedelegate)
            RxView.click(btnRedeemReward, toRedeemReward)
//            RxView.click(btnDelegateReward, toRedeemReward)
        }
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            viewBinding {
                tvName.text = description?.moniker
                tvShares.text = getDelegatorShares()
                tvSelfShares.text = getSelfDelegation()
                tvRate.text = getRate()
            }
        }
    }

    companion object {
        fun start(context: Context, validator: Validator, showDelegation: Boolean = false) {
            val intent = Intent(context, ValidatorDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            intent.putExtra(ExtraKey.BOOLEAN, showDelegation)
            context.startActivity(intent)
        }
    }

    val toDelegate = {
        if (validator != null) {
            DelegateActivity.start(this, validator!!)
        }
    }

    val toUndelegate = {
        val delegationInfo = validator?.delegationInfo
        if (delegationInfo != null) {
            UndelegateActivity.start(
                this@ValidatorDetailActivity,
                delegationInfo
            )
        }
    }

    val toRedelegate = {
        val delegationInfo = validator?.delegationInfo
        if (delegationInfo != null) {
            RedelegateActivity.start(
                this,
                delegationInfo
            )
        }
    }

    val toRedeemReward = {
        val validatorAddress = validator?.delegationInfo?.validator_address
        val delegatorAddress = validator?.delegationInfo?.delegator_address
        val reward = validator?.reward?.getReward()
        if (!TextUtils.isEmpty(validatorAddress)
            && !TextUtils.isEmpty(delegatorAddress)
            && !TextUtils.isEmpty(reward)
        ) {
            RedeemRewardActivity.start(
                this,
                validatorAddress!!,
                delegatorAddress!!,
                reward + "DIP"
            )
        }
    }
}