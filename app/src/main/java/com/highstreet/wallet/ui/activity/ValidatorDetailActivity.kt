package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityValidatorDetailBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint(injectViewModel = false)
class ValidatorDetailActivity :
    BaseActivity<ActivityValidatorDetailBinding, PlaceholderViewModel>() {

    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.validatorDetail)
        viewBinding {
            if (intent.getBooleanExtra(ExtraKey.BOOLEAN, true)) {
                btnDelegate.visible()
                RxView.click(btnDelegate) {
                    validator?.let {
                        DelegationActivity.start(
                            this@ValidatorDetailActivity,
                            it
                        )
                    }
                }
            } else {
                btnDelegate.gone()
            }
        }
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            viewBinding {
                tvName.text = description?.moniker
                tvShares.text = StringUtils.pdip2DIP(delegator_shares)
                tvSelfShares.text = StringUtils.pdip2DIP(self_delegation)
                tvRate.text = getRate()
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