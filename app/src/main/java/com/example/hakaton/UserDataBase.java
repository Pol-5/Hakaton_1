package com.example.hakaton;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password"; // Храним хэш пароля
    public static final String COLUMN_TEST="test";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " + COLUMN_TEST + " TEXT NOT NULL " +
                    ");";

    public UserDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    public boolean addUser(String username, String password,String test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_TEST, test);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }


    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean result = cursor != null && cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    public boolean checkTest(String username) {
        SQLiteDatabase db = this.getReadableDatabase();


        String[] columns = {COLUMN_ID};


        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_TEST + "=?";


        String[] selectionArgs = {username, "YES"};


        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);


        boolean result = cursor != null && cursor.getCount() > 0;


        if (cursor != null) {
            cursor.close();
        }


        return result;
    }

}
