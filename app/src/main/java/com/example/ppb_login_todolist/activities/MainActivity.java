package com.example.ppb_login_todolist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppb_login_todolist.fragment.AddNewTask;
import com.example.ppb_login_todolist.DialogCloseListener;
import com.example.ppb_login_todolist.R;
import com.example.ppb_login_todolist.utils.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.ppb_login_todolist.adapter.ToDoAdapter;
import com.example.ppb_login_todolist.models.ToDoModel;
import com.example.ppb_login_todolist.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;
    private Session session;
    int usersID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        db= new DatabaseHandler(this);
        db.openDatabase();
        taskList = new ArrayList<>();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        usersID = session.prefs.getInt("user",0);
        fab= findViewById(R.id.fab);
        taskList = db.getAllTasks(usersID);
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        findViewById(R.id.button_logoutMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks(usersID);
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
    public void logout(){
        session.setLoggedin(false);
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
    }
}