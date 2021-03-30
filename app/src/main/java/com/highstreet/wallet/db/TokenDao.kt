package com.highstreet.wallet.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(token: Token): Long

    @Delete
    fun delete(account: Token): Int

    @Query("DELETE FROM Token")
    fun deleteAll()

    @Query("SELECT * FROM Token")
    fun query(): List<Token>

    @Query("SELECT * FROM Token")
    fun queryAllAsLiveData(): LiveData<List<Token>>
}