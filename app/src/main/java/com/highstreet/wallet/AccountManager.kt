package com.highstreet.wallet

import com.hao.library.utils.SPUtils
import com.highstreet.wallet.cache.BalanceCache
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.db.Password

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class AccountManager private constructor() {

    /**
     * 余额变动
     */
    var refresh = 0

    /**
     * 所有账户
     */
    var accounts = ArrayList<Account>()
        private set

    /**
     * 当前账户
     */
    var account: Account? = null
        private set

    /**
     * 密码
     */
    var password: Password? = null
        private set

    /**
     * 当前账户地址
     */
    var address: String = ""
        get() {
            return account?.address ?: ""
        }
        private set

    var chain = ""
        get() {
            return account?.chain ?: ""
        }
        private set

    /**
     * 是否开始指纹验证
     */
    var fingerprint = false
        get() {
            return password?.fingerprint ?: false
        }
        private set

    /**
     * 子线程执行
     */
    fun init(): Account? {
        copy13()
        password = Db.instance().passwordDao().queryById(Constant.PASSWORD_DEFAULT_ID)
        getFirstAccount()
        return account
    }

    /**
     * @return 是否插入成功
     */
    fun addAccount(account: Account): Boolean {
        val dao = Db.instance().accountDao()
        val ret = dao.insert(account)
        getFirstAccount()
        return ret > 0
    }

    /**
     * @return 修改
     */
    fun update(account: Account): Boolean {
        val dao = Db.instance().accountDao()
        return dao.update(account) > 0
    }

    /**
     * @return 删除成功
     */
    fun deleteAccount(account: Account): Boolean {
        val dao = Db.instance().accountDao()
        val ret = dao.delete(account) > 0
        getFirstAccount()
        return ret
    }

    /**
     * 获取当前账户
     */
    private fun getFirstAccount() {
        val dao = Db.instance().accountDao()
        val list = dao.query()
        accounts.clear()
        if (list != null && list.isNotEmpty()) {
            account = list[0]
            accounts.addAll(list)
        } else {
            account = null
        }
        BalanceCache.instance().loadBalances()
    }


    fun updatePassword(password: Password): Boolean {
        this.password = password
        val dao = Db.instance().passwordDao()
        return dao.insert(password) > 0
    }

    /**
     * 兼容老版本(versionCode<14)，把老数据库的数据库插入到新的数据库
     */
    private fun copy13() {
        copyAccount()
        copyPassword()
    }

    private fun copyAccount() {
        val dao = Db.instance().accountDao()
        val list = dao.query()
        if (list.isNotEmpty()) {
            return
        }

        val baseData = App.instance.getOldDB()
        val oldData = baseData.onSelectAccounts()
        if (oldData.isEmpty()) {
            return
        }
        val newData = ArrayList<Account>(oldData.size)
        oldData.forEach {
            val c = if (Chain.DIP_MAIN.chainName == it.baseChain) {
                Chain.DIP_MAIN2.chainName
            } else if (Chain.DIP_TEST.chainName == it.baseChain) {
                Chain.DIP_TEST2.chainName
            } else {
                it.baseChain
            }
            newData.add(
                Account(
                    id = it.id,
                    uuid = it.uuid,
                    nickName = it.nickName,
                    isValidator = false,
                    address = it.address,
                    chain = c,
                    path = it.path.toInt(),
                    resource = it.resource,
                    spec = it.spec,
                    mnemonicSize = Constant.MNEMONIC_SIZE,
                    fromMnemonic = it.fromMnemonic,
                    balance = "",
                    delegateAmount = "",
                    unbondingAmount = "",
                    reward = "",
                    sequenceNumber = 0,
                    accountNumber = 0,
                    hasPrivateKey = true,
                    isFavorite = false,
                    isBackup = false,
                    pushAlarm = false,
                    fingerprint = false,
                    isLast = false,
                    createTime = 0,
                    importTime = 0,
                    sort = 0,
                    extension = ""
                )
            )
        }

        dao.insert(newData)
        val list2 = dao.query()
        if (list2.size == oldData.size) {
            baseData.onDeleteAccount()
        }
    }

    private fun copyPassword() {
        val dao = Db.instance().passwordDao()
        val password = dao.queryById(Constant.PASSWORD_DEFAULT_ID)
        if (password != null) {
            return
        }

        val baseData = App.instance.getOldDB()
        val oldPassword = baseData.onSelectPassword() ?: return

        val newPassword = Password(
            Constant.PASSWORD_DEFAULT_ID,
            oldPassword.resource,
            oldPassword.spec ?: "",
            SPUtils.get(App.instance, KEY_FINGERPRINT, false)
        )

        dao.insert(newPassword)
        val p = dao.queryById(Constant.PASSWORD_DEFAULT_ID)
        if (p != null) {
            baseData.onDeletePassword()
        }
    }

    fun refresh() {
        BalanceCache.instance().loadBalances()
        refresh++
    }

    companion object {
        private const val KEY_FINGERPRINT = "KEY_FINGERPRINT"
        private var instance: AccountManager? = null

        @Synchronized
        fun instance(): AccountManager {
            if (instance == null) {
                instance = AccountManager()
            }
            return instance!!
        }
    }
}