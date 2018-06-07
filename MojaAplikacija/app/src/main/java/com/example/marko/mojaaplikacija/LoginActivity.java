package com.example.marko.mojaaplikacija;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.database.UserSQLiteHelper;
import com.example.marko.mojaaplikacija.database.UserSQLiteHelper1;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.services.UserService;
import com.example.marko.mojaaplikacija.tools.Util;

import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private UserSQLiteHelper1 databaseHelper;
    private TextView textInputEditTextEmail;
    private TextView textInputEditTextPassword;

    public static final String MyPreferances = "MyPrefs";
    public static final String Username = "usernameKey";
    public static final String Name = "nameKey";
    private UserService userService;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Util.initDB(LoginActivity.this);
//        databaseHelper.addUser();
        userService = ServiceUtils.userService;
        textInputEditTextEmail = (TextView) findViewById(R.id.username);
        textInputEditTextPassword = (TextView) findViewById(R.id.password);
        btnLogin = findViewById(R.id.BTNlogin);

        sharedPreferences = getSharedPreferences(MyPreferances, Context.MODE_PRIVATE);
        String userNamePref = sharedPreferences.getString(Username,"");

        System.out.println(userNamePref);

        if(userNamePref.equals("")){
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = textInputEditTextEmail.getText().toString();
                    String password = textInputEditTextPassword.getText().toString();

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(checkLogin(username,password)){
//                        editor.putString(Username,username);
//                        editor.commit();
                        Login(username,password);
                    }
                }
            });
        }else{
            Intent intent = new Intent(this,PostsActivity.class);
            startActivity(intent);
        }


    }

    private boolean checkLogin(String username,String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this,"Username is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this,"Password is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void Login(final String username, final String password){
        Call<User> call = userService.login(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user == null){
                    Toast.makeText(LoginActivity.this,"Username or password is incorrect",Toast.LENGTH_SHORT).show();
                }else{
                    if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Name,user.getName());
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this,PostsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this,"Username or password is incorrect",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
