package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.visibility
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityValidatorDetailBinding
import com.highstreet.wallet.extensions.copy
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.listener.RxView

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
            RxView.click(tvAddress, this@ValidatorDetailActivity)
            RxView.click(ivCopy, this@ValidatorDetailActivity)
            RxView.click(btnDelegate, this@ValidatorDetailActivity)
            btnDelegate.visibility(intent.getBooleanExtra(ExtraKey.BOOLEAN, true))
        }
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            viewBinding {
                tvName.text = description?.moniker
                tvAddress.text = operator_address
                tvAvatar.text = getFirstLetterName()
                tvShares.text = StringUtils.pdip2DIP(delegator_shares)
                tvSelfShares.text = StringUtils.pdip2DIP(self_delegation)
                tvRate.text = getRate()
                tvProfile.text = getProfile()
            }
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                tvAddress, ivCopy -> tvAddress.text.toString().copy(this@ValidatorDetailActivity)
                btnDelegate -> validator?.let {
                    DelegationActivity.start(
                        this@ValidatorDetailActivity,
                        it
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context, validator: Validator, enableDelegate: Boolean = true) {
            val intent = Intent(context, ValidatorDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            intent.putExtra(ExtraKey.BOOLEAN, enableDelegate)
            context.startActivity(intent)
        }
    }
}