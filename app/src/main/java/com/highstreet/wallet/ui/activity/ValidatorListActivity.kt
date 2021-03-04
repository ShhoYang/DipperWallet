package com.highstreet.wallet.ui.activity

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.ui.BaseListActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.SortType
import com.highstreet.wallet.constant.ValidatorType
import com.highstreet.wallet.databinding.ActivityValidatorListBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.adapter.ValidatorAdapter
import com.highstreet.wallet.ui.vm.ValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class ValidatorListActivity :
    BaseListActivity<ActivityValidatorListBinding, Validator, ValidatorVM, ValidatorAdapter>(),
    View.OnClickListener {

    private var filterShow = false
    private var filterType = ValidatorType.ALL
    private var sortType = SortType.SHARES_DESC
    private var rateSort = SortType.RATE_DESC
    private var sharesSort = SortType.SHARES_DESC

    override fun initView() {
        super.initView()
        setTitle(R.string.validator)
        viewBinding {
            llShares.isSelected = true
            llType.setOnClickListener(this@ValidatorListActivity)
            llRate.setOnClickListener(this@ValidatorListActivity)
            llShares.setOnClickListener(this@ValidatorListActivity)
            llFilter.setOnClickListener(this@ValidatorListActivity)
            tvTypeAll.setOnClickListener(this@ValidatorListActivity)
            tvTypeBonded.setOnClickListener(this@ValidatorListActivity)
            tvTypeJailed.setOnClickListener(this@ValidatorListActivity)
        }
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        ValidatorDetailActivity.start(this, item)
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
}