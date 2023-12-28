package com.eshan.healthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.eshan.healthapp.Models.MobileNumber;
import com.eshan.healthapp.Models.User;

public class MobileNoDatabase extends SQLiteOpenHelper {

    // database info
    private static final String DB_NAME = "mobileNoDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_NAME = "mobileNoTable";

    // table column names
    private static final String ID = "id";
    private static final String MOBILE_NO = "mobileNo";


    public MobileNoDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String TABLE_CREATE_QUERY = "CREATE TABLE "+DB_TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MOBILE_NO+" TEXT);";
        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+DB_TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY);
        onCreate(sqLiteDatabase);

    }

    public long addMobileNumber(MobileNumber mobileNumber) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MOBILE_NO, mobileNumber.getMobileNumber());

        return database.insert(DB_TABLE_NAME, null, values);

    }

    public int updateMobileNumber(MobileNumber mobileNumber) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOBILE_NO, mobileNumber.getMobileNumber());

        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(mobileNumber.getId())};

        int rowsUpdated = database.update(DB_TABLE_NAME, values, whereClause, whereArgs);

        database.close();
        return rowsUpdated;
    }

    public MobileNumber getMobileNoByID(int id) {

        SQLiteDatabase database = getReadableDatabase();

        String[] columns = {MOBILE_NO};
        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = database.query(DB_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String mobileNo = getStringFromCursor(cursor, MOBILE_NO);

            cursor.close();
            database.close();

            return new MobileNumber(mobileNo);
        }

        return null; // Return null if user with the given ID is not found

    }

    private String getStringFromCursor(Cursor cursor, String columnName) {

        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {

            return cursor.getString(columnIndex);

        }

        return ""; // Return an empty string if the column is not found

    }

}
