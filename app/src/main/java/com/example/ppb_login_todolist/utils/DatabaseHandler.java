package com.example.ppb_login_todolist.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ppb_login_todolist.models.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int VERSION = 6;
    private static final String NAME = "toDoListLoginDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String USERID = "users_id";

//    public static final String database_name = "db_login";

    private SQLiteDatabase db;


    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
        db= getWritableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
                + STATUS + " INTEGER, " + USERID + " INTEGER, FOREIGN KEY " + " (" + USERID + ") " +
                "REFERENCES " + TODO_TABLE + " (" + ID + "));";
        db.execSQL(CREATE_TODO_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }
    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(USERID,task.getUser_id());
        db.insert(TODO_TABLE, null, cv);
    }
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(int usersID){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        String selectQuery = "SELECT * FROM " + TODO_TABLE + " WHERE " + USERID + "=" + usersID;
        db.beginTransaction();
        try{
            cur = db.rawQuery(selectQuery,null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
