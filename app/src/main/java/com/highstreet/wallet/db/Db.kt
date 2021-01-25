package com.highstreet.wallet.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.highstreet.wallet.App

/**
 * @author Yang Shihao
 */
@Database(entities = [Account::class, Password::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun passwordDao(): PasswordDao

    companion object {

        private var instance: Db? = null

        @Synchronized
        fun instance(): Db {
            @Synchronized
            if (instance == null) {
                instance = Room.databaseBuilder(
                        App.instance,
                        Db::class.java,
                        "DipperWallet"
                ).build()
            }

            return instance!!
        }
    }
}