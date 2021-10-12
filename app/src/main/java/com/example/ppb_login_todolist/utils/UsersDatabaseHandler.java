package com.example.ppb_login_todolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 6;
    private static final String NAME = "toDoListLoginDatabase";
    public static final String table_name = "login_table";
    public static final String ID = "id";
    public static final String row_username = "Username";
    public static final String row_password = "Password";
    private SQLiteDatabase db;
    public UsersDatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + table_name + " (" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + row_username + " TEXT,"
                + row_password + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }
    public void insertData(ContentValues values){
        db.insert(table_name, null, values);
    }

    public boolean checkUser(String username, String password){
        String[] columns = {ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = row_username + "=?" + " and " + row_password + "=?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.query(table_name, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0)
            return true;
        else
            return false;
    }

    public Boolean checkUser(String username){
        String[] columns = {ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = row_username + "=?" ;
        String[] selectionArgs = {username};
        Cursor cursor = db.query(table_name, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
//        db.close();

        if (count>0)
            return true;
        else
            return false;
    }
    public int getIdUser(String _email, String _password) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + ID + " FROM " + table_name + " WHERE " + row_username + " = ? AND " + row_password + " = ?";
        Cursor cursor;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {_email, _password});

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return 0;
            }
        }
        return 0;
    }



}
