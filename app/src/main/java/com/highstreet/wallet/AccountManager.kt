package com.highstreet.wallet

import com.hao.library.utils.SPUtils
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

    var chain = Chain.DIP_MAIN
        private set

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
        var isTest = BuildConfig.testnet
        chain = if (isTest) Chain.DIP_TEST else Chain.DIP_MAIN
        copy13()
        password = Db.instance().passwordDao().queryById(Constant.PASSWORD_DEFAULT_ID)
        getLaseAccount()
        return account
    }

    /**
     * @return 是否插入成功
     */
    fun addAccount(account: Account): Boolean {
        val dao = Db.instance().accountDao()
        val ret = dao.insert(account)
        changeLastAccount(account)
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
        getLaseAccount()
        return ret
    }

    /**
     * 改变当前账户
     */
    fun changeLastAccount(lastAccount: Account): Boolean {
        val dao = Db.instance().accountDao()
        val list = dao.queryByChain(chain)
        accounts.clear()
        accounts.addAll(list)
        accounts.forEach { it.isLast = it.address == lastAccount.address }
        dao.updateAll(accounts)
        account = lastAccount
        return true
    }

    /**
     * 获取当前账户
     */
    private fun getLaseAccount() {
        val dao = Db.instance().accountDao()
        val list = dao.queryByChain(chain)
        var lastAccount: Account? = list.find { it.isLast }
        if (null == lastAccount && list.isNotEmpty()) {
            lastAccount = list[0]
            lastAccount.isLast = true
            dao.update(lastAccount)
        }
        account = lastAccount
        accounts.clear()
        accounts.addAll(list)
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
        val list = dao.queryByChain(chain)
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
            newData.add(
                Account(
                    id = it.id,
                    uuid = it.uuid,
                    nickName = it.nickName,
                    isValidator = false,
                    address = it.address,
                    chain = it.baseChain,
                    path = it.path.toInt(),
                    resource = it.resource,
                    spec = it.spec,
                    mnemonicSize = Constant.MNEMONIC_SIZE,
                    fromMnemonic = it.fromMnemonic,
                    balance = "",
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
        );

        dao.insert(newPassword)
        val p = dao.queryById(Constant.PASSWORD_DEFAULT_ID)
        if (p != null) {
            baseData.onDeletePassword()
        }
    }

    fun refresh() {
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