package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.visibility
import com.hao.library.ui.BaseActivity
import com.hao.library.ui.UIParams
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.constant.ProposalOpinion
import com.highstreet.wallet.databinding.ActivityProposalDetailBinding
import com.highstreet.wallet.model.res.FinalTallyResult
import com.highstreet.wallet.model.res.Proposal
import com.highstreet.wallet.ui.vm.ProposalDetailVM
import com.highstreet.wallet.utils.StringUtils
import java.math.BigDecimal
import java.text.NumberFormat

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */
@AndroidEntryPoint
class ProposalDetailActivity : BaseActivity<ActivityProposalDetailBinding, ProposalDetailVM>() {

    private var proposal: Proposal? = null

    override fun prepare(uiParams: UIParams, intent: Intent?) {
        super.prepare(uiParams, intent)
        proposal = intent?.getSerializableExtra(ExtraKey.SERIALIZABLE) as Proposal?
    }

    override fun initView() {
        setTitle(R.string.pda_proposalDetail)
        setData(proposal)
        viewBinding {
            RxView.click(btnYes, ProposalOpinion.YES, vote)
            RxView.click(btnNo, ProposalOpinion.NO, vote)
            RxView.click(btnNoWithVeto, ProposalOpinion.NO_WITH_VETO, vote)
            RxView.click(btnAbstain, ProposalOpinion.ABSTAIN, vote)
        }
    }

    override fun initData() {
        viewModel {
            proposalLD.observe(this@ProposalDetailActivity) {
                setData(it)
            }

            rateLD.observe(this@ProposalDetailActivity) {
                calculateRate(it)
            }

            opinionLD.observe(this@ProposalDetailActivity) {
                vb?.tvOpinion?.text = ProposalOpinion.getOpinion(this@ProposalDetailActivity, it)
            }

            voteLD.observe(this@ProposalDetailActivity) {
                hideLoading()
                toast(it.second)
            }
        }

        proposal?.apply {
            viewModel {
                votingRate(id)
                proposalOpinion(id)
            }
        }
    }

    private fun setData(proposal: Proposal?) {
        proposal?.apply {
            viewBinding {
                statePoint.setBackgroundResource(if (isPassed()) R.drawable.shape_circle_green else R.drawable.shape_circle_red)
                tvState.text = getState(this@ProposalDetailActivity)
                tvTitle.text = "# ${id}.${content?.value?.title}"
                tvDesc.text = content?.value?.description
                tvProposer.text = proposer
                tvType.text = content?.type
                tvVotingStartTime.text = StringUtils.utc2String(voting_start_time)
                tvVotingEndTime.text = StringUtils.utc2String(voting_end_time)
                llVote.visibility(isVotingPeriod())
            }
        }
    }

    private fun calculateRate(finalTallyResult: FinalTallyResult?) {
        finalTallyResult?.let {
            val yes = BigDecimal(it.yes ?: "0")
            val noWithVeto = BigDecimal(it.no_with_veto ?: "0")
            val no = BigDecimal(it.no ?: "0")
            val abstain = BigDecimal(it.abstain ?: "0")
            val total = yes.add(noWithVeto).add(no).add(abstain)
            viewBinding {
                if (total.compareTo(BigDecimal(0)) == 0) {
                    tvYes.text = "0%"
                    progressYes.progress = 0

                    tvNoWithVeto.text = "0%"
                    progressNoWithVeto.progress = 0

                    tvNo.text = "0%"
                    progressNo.progress = 0

                    tvAbstain.text = "0%"
                    progressAbstain.progress = 0
                } else {
                    val maxProcess = BigDecimal(100)

                    val yesRate = yes.divide(total)
                    val noWithVetoRate = noWithVeto.divide(total)
                    val noRate = no.divide(total)
                    val abstainRate = abstain.divide(total)
                    val df = NumberFormat.getPercentInstance()
                    df.maximumFractionDigits = 4
                    tvYes.text = df.format(yesRate)
                    progressYes.progress = yesRate.multiply(maxProcess).toInt()

                    tvNoWithVeto.text = df.format(noWithVetoRate)
                    progressNoWithVeto.progress = noWithVetoRate.multiply(maxProcess).toInt()

                    tvNo.text = df.format(noRate)
                    progressNo.progress = noRate.multiply(maxProcess).toInt()

                    tvAbstain.text = df.format(abstainRate)
                    progressAbstain.progress = abstainRate.multiply(maxProcess).toInt()
                }
            }
        }
    }

    private val vote: (String) -> Unit = {
        proposal?.apply {
            showLoading()
            vm?.vote(id, it)
        }
    }

    companion object {
        fun start(context: Context, proposal: Proposal) {
            val intent = Intent(context, ProposalDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, proposal)
            context.startActivity(intent)
        }
    }
}