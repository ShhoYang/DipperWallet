package com.highstreet.wallet.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.highstreet.wallet.App

/**
 * @author Yang Shihao
 */
@Database(entities = [Account::class, Token::class, Password::class], version = 2)
abstract class Db : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun tokenDao(): TokenDao

    abstract fun passwordDao(): PasswordDao

    companion object {

        private var instance: Db? = null

        @Synchronized
        fun instance(): Db {
            @Synchronized
            if (instance == null) {
                instance = Room.databaseBuilder(App.instance, Db::class.java, "DipperWallet")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }

            return instance!!
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `Token` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `isNative` INTEGER NOT NULL, `chain` TEXT NOT NULL, `icon` TEXT NOT NULL, `name` TEXT NOT NULL, `desc` TEXT NOT NULL, `decimalPlaces` INTEGER NOT NULL, `address` TEXT NOT NULL, `symbol` TEXT NOT NULL, `balance` TEXT NOT NULL, `balanceAmount` TEXT NOT NULL, `delegateAmount` TEXT NOT NULL, `unbondingAmount` TEXT NOT NULL, `reward` TEXT NOT NULL, `extension` TEXT NOT NULL)")
            }
        }
    }
}
