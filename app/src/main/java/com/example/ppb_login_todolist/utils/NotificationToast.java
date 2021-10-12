package com.example.ppb_login_todolist.utils;

import android.content.Context;
import android.widget.Toast;

public class NotificationToast {
    public static void showToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
