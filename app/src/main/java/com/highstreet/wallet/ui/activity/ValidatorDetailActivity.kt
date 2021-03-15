package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
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
    BaseActivity<ActivityValidatorDetailBinding, PlaceholderViewModel>(), View.OnClickListener {

    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.validatorDetail)
        viewBinding {
            if (intent.getBooleanExtra(ExtraKey.BOOLEAN, false)) {
                btnDelegate2.gone()
                llDelegation.visible()
            } else {
                btnDelegate2.visible()
                llDelegation.gone()
            }

            RxView.click(btnDelegate2, this@ValidatorDetailActivity)
            RxView.click(btnDelegate, this@ValidatorDetailActivity)
            RxView.click(btnUndelegate, this@ValidatorDetailActivity)
            RxView.click(btnRedelegate, this@ValidatorDetailActivity)
            RxView.click(btnRedeemReward, this@ValidatorDetailActivity)
            RxView.click(btnDelegateReward, this@ValidatorDetailActivity)

            RxView.click(btnDelegate) {
                validator?.let {
                    DelegateActivity.start(
                        this@ValidatorDetailActivity,
                        it
                    )
                }
            }
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

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                btnDelegate, btnDelegate2 -> {
                    if (validator != null) {
                        DelegateActivity.start(
                            this@ValidatorDetailActivity,
                            validator!!
                        )
                    }
                }
                btnUndelegate -> {
                    val delegationInfo = validator?.delegationInfo
                    if (delegationInfo != null) {
                        UndelegateActivity.start(
                            this@ValidatorDetailActivity,
                            delegationInfo
                        )
                    }
                }
                btnRedelegate -> {
                    val delegationInfo = validator?.delegationInfo
                    if (delegationInfo != null) {
                        RedelegateActivity.start(
                            this@ValidatorDetailActivity,
                            delegationInfo
                        )
                    }
                }
                btnRedeemReward -> {
                    val validatorAddress = validator?.delegationInfo?.validator_address
                    val delegatorAddress = validator?.delegationInfo?.delegator_address
                    val reward = validator?.reward?.getReward()
                    if (!TextUtils.isEmpty(validatorAddress)
                        && !TextUtils.isEmpty(delegatorAddress)
                        && !TextUtils.isEmpty(reward)
                    ) {
                        RedeemRewardActivity.start(
                            this@ValidatorDetailActivity,
                            validatorAddress!!,
                            delegatorAddress!!,
                            reward + "DIP"
                        )
                    }
                }
            }
        }
    }
}