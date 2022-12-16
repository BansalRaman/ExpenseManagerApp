package com.developer.ramanbansal.billsplitter.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.developer.ramanbansal.billsplitter.model.Bill;
import com.developer.ramanbansal.billsplitter.model.Expen;

public class BillAdapter {

    public static final String TAG = "BillAdapter";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {Helper.COLUMN_BILL_ID,
            Helper.COLUMN_BILL_NAME,
            Helper.COLUMN_BILL_AMOUNT,
            Helper.COLUMN_BILL_SHARE,
            Helper.COLUMN_BILL_MEMBERS,
            Helper.COLUMN_BILL_EXPEN
    };

    public BillAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new Helper(context);
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void createBill(String name, String amount, double share, String members, String expenId) {
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_BILL_NAME, name);
        values.put(Helper.COLUMN_BILL_AMOUNT, amount);
        values.put(Helper.COLUMN_BILL_SHARE, share);
        values.put(Helper.COLUMN_BILL_MEMBERS, members);
        values.put(Helper.COLUMN_BILL_EXPEN, expenId);
        long insertId = mDatabase.insert(Helper.TABLE_BILL, null, values);
        Log.d(TAG, "" + insertId);
        mDatabase.query(Helper.TABLE_BILL, mAllColumns,
                Helper.COLUMN_BILL_ID + " = " + insertId, null, null,
                null, null);
    }

    public Cursor getBill(int expenId) {
        /*Cursor cursor = mDatabase.query(Helper.TABLE_BILL, mAllColumns, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;*/
        Log.d(TAG, "SELECT * FROM " + Helper.TABLE_BILL + " WHERE " + Helper.COLUMN_BILL_EXPEN + " = " + expenId + ";");
        Cursor result = mDatabase.rawQuery("SELECT * FROM " + Helper.TABLE_BILL + " WHERE " + Helper.COLUMN_BILL_EXPEN + " = " + expenId + ";", null);
        return result;
    }

    private Bill cursorToBill(Cursor cursor) {
        Bill bill = new Bill();
        bill.setName(cursor.getString(1));
        bill.setBillAmount(cursor.getLong(2));
        bill.setBillShare(cursor.getLong(3));
        bill.setBillMembers(cursor.getString(4));
        long expenID = cursor.getLong(5);
        ExpenAdapter adapter = new ExpenAdapter(mContext);
        Expen expen = adapter.getExpenById(expenID);
        if (expen != null)
            bill.setExpenId(expen.getId());
        return bill;
    }

}
