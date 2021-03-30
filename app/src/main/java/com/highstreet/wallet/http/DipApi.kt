package com.highstreet.wallet.http

import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.EstimateGas
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

interface DipApi {

    /**
     * 水龙头
     */
    @GET
    fun test(@Url url: String): Observable<Any>

    /**
     * 价格
     */
    @GET("https://api.coingecko.com/api/v3/simple/price?ids=dipper-network&vs_currencies=usd,eur,krw,jpy,cny,btc")
    fun price(): Observable<CoinPrice>

    /**
     * 节点信息
     */
    @GET("node_info")
    fun nodeInfo(): Observable<NodeInfoBean>

    /**
     * 转账，委托
     */
    @POST("txs")
    fun txs(@Body ReqBroadCast: RequestBroadCast): Observable<Tx>

    /**
     * 账户信息
     */
    @GET("auth/accounts/{address}")
    fun balance(@Path("address") address: String): Observable<BaseBean<AccountInfo>>

    /**
     * 账户信息
     */
    @GET("auth/accounts/{address}")
    fun account(@Path("address") address: String): Observable<BaseBean<AccountInfo>>

    /**
     * 历史记录
     */
    @GET("https://api.dippernetwork.com/v1/account/txs/{address}")
    fun txHistory(
        @Path("address") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<ArrayList<Tx>>

    /**
     * 入账记录
     * txs?transfer.recipient=地址page=1&limit=100
     */
    @GET("txs")
    fun transactionInRecord(
        @Query("transfer.recipient") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<Transaction>

    /**
     * 出账记录
     * txs?message.action=send&message.sender=地址&page=1&limit=100
     */
    @GET("txs")
    fun transactionOutRecord(
        @Query("message.sender") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int,
        @Query("message.action") action: String = "send"
    ): Observable<Transaction>

    /**
     * 查询通胀率
     */
    @GET("minting/inflation")
    fun getInflation(): Observable<BaseBean<String>>

    /**
     * staking交易
     */
    @GET("staking/delegators/{address}/txs")
    fun delegationTransactionRecord(
        @Path("address") address: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<BaseBean<ArrayList<Transaction>>>

    /**
     * 委托信息
     */
    @GET("staking/delegators/{address}/delegations")
    fun delegations(
        @Path("address") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<BaseBean<ArrayList<DelegationInfo>>>

    /**
     * 正在解除的委托信息
     */
    @GET("staking/delegators/{address}/unbonding_delegations")
    fun unbondingDelegations(
        @Path("address") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<BaseBean<ArrayList<DelegationInfo>>>

    /**
     * 所有验证人
     */
    @GET("staking/validators")
    fun validators(
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<BaseBean<ArrayList<Validator>>>

    /**
     * 地址绑定的所有验证人
     */
    @GET("staking/delegators/{address}/validators")
    fun validatorsByAddress(
        @Path("address") address: String,
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Observable<BaseBean<ArrayList<Validator>>>

    /**
     * 获取验证人信息
     */
    @GET("staking/validators/{validatorAddress}")
    fun validatorDetail(@Path("validatorAddress") validatorAddress: String): Observable<BaseBean<Validator>>

    /**
     * 查询收益
     */
    @GET("distribution/delegators/{address}/rewards")
    fun rewards(@Path("address") address: String): Observable<BaseBean<Rewards>>

    /**
     * 根据委托人查询收益
     */
    @GET("distribution/delegators/{address}/rewards/{validatorAddress}")
    fun rewardsByValidator(
        @Path("address") address: String,
        @Path("validatorAddress") validatorAddress: String
    ): Observable<BaseBean<ArrayList<Coin>>>

    /**
     * 提案
     */
    @GET("gov/proposals")
    fun proposals(): Observable<BaseBean<ArrayList<Proposal>>>

    /**
     * 提案详情
     */
    @GET("gov/proposals/{proposalId}")
    fun proposalDetail(@Path("proposalId") proposalId: String): Observable<BaseBean<Proposal>>

    /**
     * 投票比例
     */
    @GET("gov/proposals/{proposalId}/tally")
    fun votingRate(@Path("proposalId") proposalId: String): Observable<BaseBean<FinalTallyResult>>

    /**
     * 我的投票
     */
    @GET("gov/proposals/{proposalId}/votes/{voter}")
    fun proposalOpinion(
        @Path("proposalId") proposalId: String,
        @Path("voter") voter: String
    ): Observable<BaseBean<ProposalOpinion>>

    /**
     * 合约代码
     */
    @GET("vm/code/{address}")
    fun contractCode(@Path("address") address: String): Observable<BaseBean<String>>

    /**
     * 合约代码
     */
    @GET("vm/code/{address}")
    fun tokenBalance(): Observable<BaseBean<String>>

    /**
     * 预估交易费用
     */
    @POST("vm/estimate_gas")
    fun estimateGas(@Body estimateGas: EstimateGas): Observable<BaseBean<Gas>>

}

//https://rpc.testnet.dippernetwork.com/minting/inflation
//https://rpc.testnet.dippernetwork.com/minting/parameters
//https://rpc.testnet.dippernetwork.com/staking/delegators/dip1fl50wmzvpnhtz0f8qdzmhr0lz4gve3qjtyty8k/delegations
//https://rpc.testnet.dippernetwork.com/staking/delegators/dip1fl50wmzvpnhtz0f8qdzmhr0lz4gve3qjtyty8k/unbonding_delegations
//https://rpc.testnet.dippernetwork.com/staking/delegators/dip1fl50wmzvpnhtz0f8qdzmhr0lz4gve3qjtyty8k/txs
//https://rpc.testnet.dippernetwork.com/staking/delegators/dip1fl50wmzvpnhtz0f8qdzmhr0lz4gve3qjtyty8k/validators
