package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DATEFormat;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDATEFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.DATE;
import java.util.LinkedList;
import android.content.ContentValues;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseTYP;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantMemoryTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {

    public static final String DATABASE_NAME = "170410X.db";
    public static final String EXPENSE_COLUMN_ID = "ID";
    public static final String EXPENSE_COLUMN_NO = "ACCNUM";
    public static final String EXPENSE_COLUMN_DATE = "DATE";
    public static final String EXPENSE_COLUMN_TYP = "TYP";
    public static final String EXPENSE_COLUMN_AMOUNT = "AMOUNT";

    private List<Transaction> transactions;

    public PersistantMemoryTransactionDAO(Context context) {
        super(context, DATABASE_NAME , null,1);
        transactions = new LinkedList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_tbltrans_TABLE = "CREATE TABLE"+ tbltrans + "(" +EXPENSE_COLUMN_ID +"INTEGER PRIMARY,"+EXPENSE_COLUMN_NO +"VARCHAR,"+EXPENSE_COLUMN_Date+"DATE,"+EXPENSE_COLUMN_Type+"TEXT,"+EXPENSE_COLUMN_Amount+"DECIMAL"+ ")";
db.excecSQL(CREATE_tbltrans_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       
        db.execSQL("DROP TABLE IF EXISTS tbltrans");
        onCreate(db);
    }


    @Override
    public void logTransaction(DATE DATE, String ACCNUM, ExpenseTYP expenseTYP, double AMOUNT) {
        Transaction transaction = new Transaction(DATE, ACCNUM, expenseTYP, AMOUNT);
        String accountNumber = transaction.getACCNUM();
        DATE DATEs = transaction.getDATE();

        byte[] byteDATE = DATEs.toString().getBytes();
        ExpenseTYP TYPs = transaction.getExpenseTYP();
        String strTYP = TYPs.toString();
        byte[] byteTYP = toString().getBytes();
        Double AMOUNTs = transaction.getAMOUNT();

        Calendar c = Calendar.getInstance();


        SimpleDATEFormat df = new SimpleDATEFormat("dd-MMM-yyyy");
        String formattedDATE = df.format(c.getTime());
        Log.d("DATE",formattedDATE);
        byte[] timeStamp = formattedDATE.getBytes();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCNUM", ACCNUM);
        contentValues.put("AMOUNT", AMOUNTs);
        contentValues.put("TYP",strTYP);
        contentValues.put("DATE", byteDATE);


        db.insert("tbltrans", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        transactions.clear();
        Log.d("creation","starting");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( " select * from tbltrans", null );

        res.moveToFirst();

        while(res.isAfterLast() == false){

            String ACCNUM = res.getString(res.getColumnIndex(EXPENSE_COLUMN_NO));
            Double AMOUNT = res.getDouble(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
            String transTYP = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TYP));

            ExpenseTYP TYP = ExpenseTYP.valueOf(transTYP);
            byte[] DATE = res.getBlob(res.getColumnIndex(EXPENSE_COLUMN_DATE));


            String str = new String(DATE, StandardCharsets.UTF_8);
            Log.d("loadedDATE",str);

            DATE finalDATE;
            try {


                SimpleDATEFormat inputFormat = new SimpleDATEFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.ENGLISH);
                finalDATE = inputFormat.parse(str);
                transactions.add(new Transaction(finalDATE,ACCNUM,TYP,AMOUNT));
                Log.d("creation","success");
            }catch (java.text.ParseException e){
                Log.d("creation","failed");
                Calendar cal = Calendar.getInstance();

                finalDATE = cal.getTime();
                transactions.add(new Transaction(finalDATE,ACCNUM,TYP,AMOUNT));

            }


            res.moveToNext();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        
        return transactions.subList(size - limit, size);
    }




}


