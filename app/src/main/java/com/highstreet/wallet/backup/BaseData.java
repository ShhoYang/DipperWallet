package com.highstreet.wallet.backup;


import android.app.Application;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class BaseData {

    private final Application mApp;
    private SQLiteDatabase mSQLiteDatabase;


    public BaseData(Application apps) {
        this.mApp = apps;
        SQLiteDatabase.loadLibs(mApp);
    }

    public SQLiteDatabase getBaseDB() {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = BaseDB.getInstance(mApp).getWritableDatabase("wannabit");
        }
        return mSQLiteDatabase;
    }

    public ArrayList<Account> onSelectAccounts() {
        ArrayList<Account> result = new ArrayList<>();
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_ACCOUNT, new String[]{"id", "uuid",
                "nickName", "isFavo", "address", "baseChain", "hasPrivateKey", "resource", "spec",
                "fromMnemonic", "path", "isValidator", "sequenceNumber", "accountNumber",
                "fetchTime", "msize", "importTime", "lastTotal", "sortOrder", "pushAlarm",
                "newBip"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3) > 0,
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6) > 0,
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getInt(9) > 0,
                        cursor.getString(10),
                        cursor.getInt(11) > 0,
                        cursor.getInt(12),
                        cursor.getInt(13),
                        cursor.getLong(14),
                        cursor.getInt(15),
                        cursor.getLong(16),
                        cursor.getString(17),
                        cursor.getLong(18),
                        cursor.getInt(19) > 0,
                        cursor.getInt(20) > 0
                );
                result.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public boolean onDeleteAccount() {
        return getBaseDB().delete(BaseConstant.DB_TABLE_ACCOUNT, "", new String[]{}) > 0;
    }

    public Password onSelectPassword() {
        Password result = null;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_PASSWORD, new String[]{"resource", "spec"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result = new Password(cursor.getString(0), cursor.getString(1));
        }
        cursor.close();
        return result;
    }

    public boolean onDeletePassword() {
        return getBaseDB().delete(BaseConstant.DB_TABLE_PASSWORD, "", new String[]{}) > 0;
    }

}
