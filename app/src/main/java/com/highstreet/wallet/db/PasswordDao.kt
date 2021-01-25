package com.highstreet.wallet.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(password: Password): Long

    @Query("DELETE FROM Password")
    fun deleteAll()

    @Query("SELECT * FROM Password WHERE id = (:id) LIMIT 1")
    fun queryById(id: Long): Password?

    @Query("SELECT * FROM Password WHERE id = (:id) LIMIT 1")
    fun queryByIdAsLiveData(id: Long): LiveData<Password?>

    @Query("SELECT * FROM Password")
    fun queryAll(): List<Password>
}