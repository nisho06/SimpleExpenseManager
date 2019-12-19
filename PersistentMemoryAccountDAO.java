/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentMemoryAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    //private final Map<String, Account> accounts;

    public static final String DATABASE_NAME = "170410X.db";
    public static final String CONTACTS_COLUMN_NO = "ACCNUM";
    public static final String CONTACTS_COLUMN_BANK_NAME = "BankName";
    public static final String CONTACTS_COLUMN_HOLDER_NAME = "HolderName";
    public static final String CONTACTS_COLUMN_BAL = "BAL";

    public PersistentMemoryAccountDAO(Context context) {
        super(context, DATABASE_NAME , null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table account " +
                        "(ACCNUM text primary key, BankName text,HolderName text,BAL double)"
        );
        db.execSQL(
                "create table tbltrans " +
                        "(ACCNUM text, type text, date BLOB , amount double)"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS account");
        onCreate(db);
    }

//    public PersistentMemoryAccountDAO() {
//        this.accounts = new HashMap<>();
//    }


    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO)));
            res.moveToNext();
        }
        return array_list;


    }

    @Override
    public List<Account> getAccountsList()
    {
        ArrayList<Account> array_list = new ArrayList<Account>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String ACCNUM = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO));
            String BankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
            String accountHolderName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_HOLDER_NAME));
            Double BAL = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BAL));

            array_list.add(new Account(ACCNUM,BankName,accountHolderName,BAL));
            res.moveToNext();
        }
        return array_list;
    }

    @Override
    public Account getAccount(String ACCNUM) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account where id="+ACCNUM+"", null );

        String ACCNUM = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO));
        String BankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
        String accountHolderName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_HOLDER_NAME));
        Double BAL = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BAL));

        return  new Account(ACCNUM,BankName,accountHolderName,BAL);


    }

    @Override
    public void addAccount(Account account) {
        String ACCNUM = account.getACCNUM();
        String BankName = account.getBankName();
        String HolderName = account.getAccountHolderName();
        Double BAL = account.getBAL();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCNUM", ACCNUM);
        contentValues.put("BankName", BankName);
        contentValues.put("HolderName", HolderName);
        contentValues.put("BAL", BAL);

        db.insert("account", null, contentValues);

    }

    @Override
    public void removeAccount(String ACCNUM) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("account",
                "ACCNUM = ? ",
                new String[] { ACCNUM});
    }

    @Override
    public void updateBAL(String ACCNUM, ExpenseType expenseType, double amount) throws InvalidAccountException {
//        if (!accounts.containsKey(ACCNUM)) {
//            String msg = "Account " + ACCNUM + " is invalid.";
//            throw new InvalidAccountException(msg);
//        }
//        Account account = accounts.get(ACCNUM);
//        // specific implementation based on the transaction type
//        switch (expenseType) {
//            case EXPENSE:
//                account.setBAL(account.getBAL() - amount);
//                break;
//            case INCOME:
//                account.setBAL(account.getBAL() + amount);
//                break;
//        }
//        accounts.put(ACCNUM, account);
    }
}
