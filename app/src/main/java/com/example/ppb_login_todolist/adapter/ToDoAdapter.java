package com.example.ppb_login_todolist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppb_login_todolist.fragment.AddNewTask;
import com.example.ppb_login_todolist.activities.MainActivity;
import com.example.ppb_login_todolist.models.ToDoModel;
import com.example.ppb_login_todolist.R;
import com.example.ppb_login_todolist.utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        updateTaskListener(holder);
        deleteTaskListener(holder);
        checkBoxlistener(holder,item);

//        holder.updateTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editItem(holder.getAdapterPosition());
//            }
//        });
//        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Delete Task");
//                builder.setMessage("Are you sure you want to delete this Task?");
//                builder.setPositiveButton("Confirm",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                deleteItem(holder.getAdapterPosition());
//                            }
//                        });
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        notifyItemChanged(holder.getAdapterPosition());
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
//        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    db.updateStatus(item.getId(),1);
//                }
//                else{
//                    db.updateStatus(item.getId(),0);
//                }
//            }
//        });
    }
    @Override
    public int getItemCount(){
        return todoList.size();
    }
    private void updateTaskListener(ViewHolder holder){
        holder.updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(holder.getAdapterPosition());
            }
        });
    }
    private void deleteTaskListener(ViewHolder holder){
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this Task?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(holder.getAdapterPosition());
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void checkBoxlistener(ViewHolder holder,ToDoModel item){
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                    holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.task.setTextColor(Color.argb(128, 0, 0, 0));
                    holder.task.setChecked(true);

                }
                else{
                    db.updateStatus(item.getId(),0);
                    holder.task.setPaintFlags(holder.task.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.task.setTextColor(Color.argb(255, 0, 0, 0));
                    holder.task.setChecked(false);
                }
            }
        });
    }
    private boolean toBoolean(int n){
        return n !=0 ;
    }
    public Context getContext(){
        return activity;
    }
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public void setTasks(List<ToDoModel> todoList){
        this.todoList= todoList;
        notifyDataSetChanged();
    }
    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ImageButton updateTask;
        ImageButton deleteTask;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            updateTask = view.findViewById(R.id.editBtn);
            deleteTask = view.findViewById(R.id.deleteBtn);
        }
    }
}
