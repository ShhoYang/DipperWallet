package com.highstreet.wallet.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account): Long

    @Delete
    fun delete(account: Account): Int

    @Update
    fun update(account: Account): Int

    @Update
    fun updateAll(accounts: List<Account>)

    @Query("SELECT * FROM Account WHERE id = (:id) LIMIT 1")
    fun queryById(id: Long): LiveData<Account?>

    @Query("SELECT * FROM Account WHERE isLast = (:isLast) LIMIT 1")
    fun queryLastUserAsLiveData(isLast: Boolean): LiveData<Account?>

    @Query("SELECT * FROM Account WHERE chain = (:chain)")
    fun queryByChain(chain: String): List<Account>

    @Query("SELECT * FROM Account WHERE chain = (:chain)")
    fun queryAllByChainAsLiveData(chain: String): LiveData<List<Account>>

    @Query("SELECT * FROM Account")
    fun query(): List<Account>

    @Query("SELECT * FROM Account")
    fun queryAllAsLiveData(): LiveData<List<Account>>
}