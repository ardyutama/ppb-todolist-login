package com.example.ppb_login_todolist.activities;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppb_login_todolist.R;
import com.example.ppb_login_todolist.utils.DatabaseHandler;
import com.example.ppb_login_todolist.utils.NotificationToast;
import com.example.ppb_login_todolist.utils.Session;
import com.example.ppb_login_todolist.utils.UsersDatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    private EditText mViewUser, mViewPassword;
    UsersDatabaseHandler usersDatabaseHandler;
    private Session session;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);
        sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        sharedPreferences.contains("username");
        mViewUser=findViewById(R.id.et_emailSignin);
        mViewPassword =findViewById(R.id.et_passwordSignin);
        usersDatabaseHandler = new UsersDatabaseHandler(this);

        mViewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    validationLogin();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.button_signinSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationLogin();
            }
        });

        findViewById(R.id.button_signupSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
    }

    private void validationLogin(){
        String user = mViewUser.getText().toString();
        String password = mViewPassword.getText().toString();
        Boolean res = usersDatabaseHandler.checkUser(user,password);
        int userID = usersDatabaseHandler.getIdUser(user,password);
        mViewUser.setError(null);
        mViewPassword.setError(null);
        if(res == true){
            session.setLoggedin(true);
            session.editor.putInt("user",userID);
            session.editor.apply();
            NotificationToast.showToast(LoginActivity.this,"Login Success");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else {
            NotificationToast.showToast(LoginActivity.this,"Login Failed");
        }
    }
}
