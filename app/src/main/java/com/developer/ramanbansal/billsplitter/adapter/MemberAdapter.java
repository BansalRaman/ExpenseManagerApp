package com.developer.ramanbansal.billsplitter.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.developer.ramanbansal.billsplitter.model.Member;
import com.developer.ramanbansal.billsplitter.model.Expen;

public class MemberAdapter {

    public static final String TAG = "MemberAdapter";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {Helper.COLUMN_MEMBER_ID,
            Helper.COLUMN_MEMBER_NAME,
            Helper.COLUMN_MEMBER_BALANCE,
            Helper.COLUMN_MEMBER_EXPEN};

    public MemberAdapter(Context context) {
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

    public Member createMember(String name, int expenID) {
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_MEMBER_NAME, name);
        values.put(Helper.COLUMN_MEMBER_BALANCE, 0);
        values.put(Helper.COLUMN_MEMBER_EXPEN, expenID);
        long insertId = mDatabase.insert(Helper.TABLE_MEMBER, null, values);
        Cursor cursor = mDatabase.query(Helper.TABLE_MEMBER, mAllColumns,
                Helper.COLUMN_MEMBER_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Member newMember = cursorToMember(cursor);
        cursor.close();
        return newMember;
    }

    public Cursor getMember(String expenID) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + Helper.TABLE_MEMBER + " WHERE " + Helper.COLUMN_MEMBER_EXPEN + " = " + expenID + ";", null);
        Log.d("", "SELECT * FROM " + Helper.TABLE_MEMBER + " WHERE " + Helper.COLUMN_MEMBER_EXPEN + " = " + expenID + ";");
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Member cursorToMember(Cursor cursor) {
        Member member = new Member();
        member.setId(cursor.getLong(0));
        member.setName(cursor.getString(1));
        member.setBalance(cursor.getDouble(2));
        long expenID = cursor.getLong(3);
        Log.d("Cursor Member", "" + cursor.getDouble(2));
        ExpenAdapter adapter = new ExpenAdapter(mContext);
        Expen expen = adapter.getExpenById(expenID);
        if (expen != null)
            member.setExpenId(expen.getId());
        return member;
    }

    public boolean deleteMemberById(String memberID) {
        return mDatabase.delete(Helper.TABLE_MEMBER, Helper.COLUMN_MEMBER_ID + " = ?", new String[]{memberID}) > 0;
    }

    public double getBalance(String expenId, String name) {
        String query = "SELECT " + Helper.COLUMN_MEMBER_BALANCE + " FROM member WHERE " + Helper.COLUMN_MEMBER_NAME + " = '" + name + "' and " + Helper.COLUMN_MEMBER_EXPEN + " = " + expenId + ";";
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        double amt = cursor.getDouble(0);
        return amt;
    }


    public void updateBalance(String expenId, String name, double amount) {
        ContentValues cv = new ContentValues();
        cv.put(Helper.COLUMN_MEMBER_BALANCE, amount);
        mDatabase.update(Helper.TABLE_MEMBER, cv, Helper.COLUMN_MEMBER_NAME + "='" + name + "' and " + Helper.COLUMN_MEMBER_EXPEN + "=" + expenId, null);
    }
}
