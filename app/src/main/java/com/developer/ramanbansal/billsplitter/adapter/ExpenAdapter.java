package com.developer.ramanbansal.billsplitter.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.developer.ramanbansal.billsplitter.model.Expen;

import java.util.ArrayList;
import java.util.List;

public class ExpenAdapter {

    public static final String TAG = "ExpenAdapter";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {Helper.COLUMN_EXPEN_ID,
            Helper.COLUMN_EXPEN_NAME};

    public ExpenAdapter(Context context) {
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

    public Expen createExpen(String name) {
        open();
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_EXPEN_NAME, name);
        long insertId = mDatabase.insert(Helper.TABLE_EXPEN, null, values);
        Cursor cursor = mDatabase.query(Helper.TABLE_EXPEN, mAllColumns,
                Helper.COLUMN_EXPEN_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Expen newExpen = cursorToExpen(cursor);
        cursor.close();
        return newExpen;
    }

    public void deleteExpen(Expen expen) {
        long id = expen.getId();
        /*
        // delete all employees of this company
        EmployeeDAO employeeDao = new EmployeeDAO(mContext);
        List<Employee> listEmployees = employeeDao.getEmployeesOfCompany(id);
        if (listEmployees != null && !listEmployees.isEmpty()) {
            for (Employee e : listEmployees) {
                employeeDao.deleteEmployee(e);
            }
        }
        */
        mDatabase.delete(Helper.TABLE_EXPEN, Helper.COLUMN_EXPEN_ID + " = " + id, null);
    }

    public List<Expen> getAllExpen() {
        List<Expen> listExpen = new ArrayList<Expen>();
        Cursor cursor = mDatabase.query(Helper.TABLE_EXPEN, mAllColumns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Expen expen = cursorToExpen(cursor);
                listExpen.add(expen);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listExpen;
    }

    public Cursor getExpen() {
        Cursor cursor = mDatabase.query(Helper.TABLE_EXPEN, mAllColumns, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Expen getExpenById(long id) {
        Cursor cursor = mDatabase.query(Helper.TABLE_EXPEN, mAllColumns, Helper.COLUMN_EXPEN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Expen expen = cursorToExpen(cursor);
        return expen;
    }

    private Expen cursorToExpen(Cursor cursor) {
        Expen expen = new Expen();
        expen.setId(cursor.getLong(0));
        expen.setName(cursor.getString(1));
        return expen;
    }

}
