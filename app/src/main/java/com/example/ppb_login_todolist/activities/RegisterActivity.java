package com.example.ppb_login_todolist.activities;

import android.content.ContentValues;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppb_login_todolist.R;
import com.example.ppb_login_todolist.utils.NotificationToast;
import com.example.ppb_login_todolist.utils.UsersDatabaseHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText mViewUser, mViewPassword, mViewRepassword;

    UsersDatabaseHandler usersDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usersDatabaseHandler = new UsersDatabaseHandler(this);

        mViewUser =findViewById(R.id.et_emailSignup);
        mViewPassword =findViewById(R.id.et_passwordSignup);
        mViewRepassword =findViewById(R.id.et_passwordSignup2);

        mViewRepassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    checkRegister();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.button_signupSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });
    }
    private boolean cekPassword(String password, String repassword){
        return password.equals(repassword);
    }

    private boolean cekUser(String user){
        if (usersDatabaseHandler.checkUser(user)){
            return true;
        }
        else{
            NotificationToast.showToast(RegisterActivity.this,"Register Success");
            return false;
        }
    }

    private void checkRegister(){
        mViewUser.setError(null);
        mViewPassword.setError(null);
        mViewRepassword.setError(null);
        View fokus = null;
        boolean cancel = false;


        String repassword = mViewRepassword.getText().toString();
        String user = mViewUser.getText().toString();
        String password = mViewPassword.getText().toString();
        ContentValues values = new ContentValues();

        if (TextUtils.isEmpty(user)){
            mViewUser.setError("This field is required");
            fokus = mViewUser;
            cancel = true;
        }else if(cekUser(user)){
            mViewUser.setError("This Username is already exist");
            fokus = mViewUser;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)){
            mViewPassword.setError("This field is required");
            fokus = mViewPassword;
            cancel = true;
        }else if (!cekPassword(password,repassword)){
            mViewRepassword.setError("This password is incorrect");
            fokus = mViewRepassword;
            cancel = true;
        }

        if (cancel){
            fokus.requestFocus();
        }else{
            values.put(usersDatabaseHandler.row_username, user);
            values.put(usersDatabaseHandler.row_password, password);
            usersDatabaseHandler.insertData(values);
            finish();
        }
    }


}
