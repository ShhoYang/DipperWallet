package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.SortType
import com.highstreet.wallet.databinding.FragmentAllValidatorListBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.activity.ValidatorDetailActivity
import com.highstreet.wallet.ui.adapter.ValidatorAdapter
import com.highstreet.wallet.ui.vm.AllValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class AllValidatorListFragment :
    BaseListFragment<FragmentAllValidatorListBinding, Validator, AllValidatorVM, ValidatorAdapter>(),
    View.OnClickListener {

    private var sortType = SortType.SHARES_DESC
    private var rateSort = SortType.RATE_DESC
    private var sharesSort = SortType.SHARES_DESC

    override fun initView() {
        super.initView()
        viewBinding {
            llShares.isSelected = true
            llRate.setOnClickListener(this@AllValidatorListFragment)
            llShares.setOnClickListener(this@AllValidatorListFragment)
        }
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        act {
            ValidatorDetailActivity.start(it, item, false)
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                llRate -> clickRate()
                llShares -> clickShares()
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
        vm?.filter(sortType)
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
        vm?.filter(sortType)
    }
}