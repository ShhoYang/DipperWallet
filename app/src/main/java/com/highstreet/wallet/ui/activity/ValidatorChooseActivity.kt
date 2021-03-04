package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseListActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.constant.SortType
import com.highstreet.wallet.constant.ValidatorType
import com.highstreet.wallet.databinding.ActivityValidatorListBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.adapter.ValidatorChooseAdapter
import com.highstreet.wallet.ui.vm.ValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class ValidatorChooseActivity :
    BaseListActivity<ActivityValidatorListBinding, Validator, ValidatorVM, ValidatorChooseAdapter>(),
    View.OnClickListener {

    private var filterShow = false
    private var filterType = ValidatorType.ALL
    private var sortType = SortType.SHARES_DESC
    private var rateSort = SortType.RATE_DESC
    private var sharesSort = SortType.SHARES_DESC

    override fun initView() {
        super.initView()
        setTitle(R.string.selectValidator)
        viewBinding {
            llShares.isSelected = true
            llType.setOnClickListener(this@ValidatorChooseActivity)
            llRate.setOnClickListener(this@ValidatorChooseActivity)
            llShares.setOnClickListener(this@ValidatorChooseActivity)
            llFilter.setOnClickListener(this@ValidatorChooseActivity)
            tvTypeAll.setOnClickListener(this@ValidatorChooseActivity)
            tvTypeBonded.setOnClickListener(this@ValidatorChooseActivity)
            tvTypeJailed.setOnClickListener(this@ValidatorChooseActivity)
        }
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        if (R.id.ivArrow == view.id) {
            ValidatorDetailActivity.start(this, item, false)
        } else {
            val intent = Intent()
            intent.putExtra(ExtraKey.SERIALIZABLE, item)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                llType -> clickType()
                llRate -> clickRate()
                llShares -> clickShares()
                llFilter -> hideFilter()
                tvTypeAll -> selectedType(tvTypeAll.text.toString(), ValidatorType.ALL)
                tvTypeBonded -> selectedType(tvTypeBonded.text.toString(), ValidatorType.BONDED)
                tvTypeJailed -> selectedType(tvTypeJailed.text.toString(), ValidatorType.JAILED)
            }
        }
    }

    private fun hideFilter() {
        filterShow = false
        vb?.llFilter?.gone()
    }

    private fun clickType() {
        if (filterShow) {
            hideFilter()
        } else {
            filterShow = true
            viewBinding {
                llFilter.visible()
                val type = tvType.text
                tvTypeAll.isSelected = type == tvTypeAll.text
                tvTypeBonded.isSelected = type == tvTypeBonded.text
                tvTypeJailed.isSelected = type == tvTypeJailed.text
            }
        }
    }

    private fun clickRate() {
        viewBinding {
            llShares.isSelected = false
            if (llRate.isSelected) {
                rateSort = if (rateSort == SortType.RATE_ASC) {
                    SortType.RATE_DESC
                } else {
                    SortType.RATE_ASC
                }
            }
            if (rateSort == SortType.RATE_ASC) {
                ivRateArrow.setImageResource(R.drawable.selector_up)
            } else {
                ivRateArrow.setImageResource(R.drawable.selector_down)
            }
            sortType = rateSort
            llRate.isSelected = true
        }
        vm?.filter(filterType, sortType)
    }

    private fun clickShares() {
        viewBinding {
            llRate.isSelected = false
            if (llShares.isSelected) {
                sharesSort = if (sharesSort == SortType.SHARES_ASC) {
                    SortType.SHARES_DESC
                } else {
                    SortType.SHARES_ASC
                }
            }
            if (sharesSort == SortType.SHARES_ASC) {
                ivSharesArrow.setImageResource(R.drawable.selector_up)
            } else {
                ivSharesArrow.setImageResource(R.drawable.selector_down)
            }
            sortType = sharesSort
            llShares.isSelected = true
        }
        vm?.filter(filterType, sortType)
    }

    private fun selectedType(text: String, type: Int) {
        filterType = type
        vb?.tvType?.text = text
        hideFilter()
        vm?.filter(filterType, sortType)
    }

    companion object {
        const val REQUEST_CODE_VALIDATOR_CHOOSE = 302
        fun start(context: Activity) {
            val intent = Intent(context, ValidatorChooseActivity::class.java)
            context.startActivityForResult(intent, REQUEST_CODE_VALIDATOR_CHOOSE)
        }
    }
}