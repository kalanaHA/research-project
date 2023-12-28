package com.eshan.healthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.eshan.healthapp.Models.User;

import java.util.ArrayList;
import java.util.List;

public class LocaleDatabase extends SQLiteOpenHelper {

    // database info
    private static final String DB_NAME = "localeDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_NAME = "userTable";

    // table column names
    private static final String ID = "id";
    private static final String USER_NAME = "userName";
    private static final String ADDRESS = "address";
    private static final String BLOOD_TYPE = "bloodType";
    private static final String HEALTH_STATUS = "healthStatus";

    public LocaleDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String TABLE_CREATE_QUERY = "CREATE TABLE "+DB_TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_NAME+" TEXT, "+ADDRESS+
                " TEXT, "+BLOOD_TYPE+" TEXT, "+HEALTH_STATUS+" TEXT);";
        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+DB_TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY);
        onCreate(sqLiteDatabase);

    }

    public long addUser(User user) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_NAME, user.getName());
        values.put(ADDRESS, user.getAddress());
        values.put(BLOOD_TYPE, user.getBloodType());
        values.put(HEALTH_STATUS, user.getHealthStatus());

        return database.insert(DB_TABLE_NAME, null, values);

    }

    // Method to get all rows from the database as List<User>
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DB_TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                String name = getStringFromCursor(cursor, USER_NAME);
                String address = getStringFromCursor(cursor, ADDRESS);
                String bloodType = getStringFromCursor(cursor, BLOOD_TYPE);
                String healthStatus = getStringFromCursor(cursor, HEALTH_STATUS);

                User user = new User(name, address, bloodType, healthStatus);
                userList.add(user);
            }

            cursor.close();

        }

        database.close(); // Close the database connection after retrieving all users
        return userList;

    }

    public User getUserById(int userId) {

        SQLiteDatabase database = getReadableDatabase();

        String[] columns = {USER_NAME, ADDRESS, BLOOD_TYPE, HEALTH_STATUS};
        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = database.query(DB_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = getStringFromCursor(cursor, USER_NAME);
            String address = getStringFromCursor(cursor, ADDRESS);
            String bloodType = getStringFromCursor(cursor, BLOOD_TYPE);
            String healthStatus = getStringFromCursor(cursor, HEALTH_STATUS);

            cursor.close();
            database.close();

            return new User(name, address, bloodType, healthStatus);
        }

        return null; // Return null if user with the given ID is not found

    }

    public int updateUser(int userId, User newUser) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, newUser.getName());
        values.put(ADDRESS, newUser.getAddress());

        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};

        int rowsUpdated = database.update(DB_TABLE_NAME, values, whereClause, whereArgs);

        database.close();
        return rowsUpdated;
    }

    private String getStringFromCursor(Cursor cursor, String columnName) {

        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {

            return cursor.getString(columnIndex);

        }

        return ""; // Return an empty string if the column is not found

    }


}
