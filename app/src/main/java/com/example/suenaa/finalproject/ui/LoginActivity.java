package com.example.suenaa.finalproject.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private EditText nameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameET = (EditText) findViewById(R.id.nameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
        login = (Button)findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        UserDatabase.initInstance(LoginActivity.this);
        final UserDatabase db = UserDatabase.getInstance();

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里可以写一个检测为空、错误提示啥的
                String name = nameET.getText().toString();
                String password = passwordET.getText().toString();

                int id = -1;
                //检查为空
                if(name.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //判断存不存在匹配的user，给出提示
                        List<User> list = db.getUserByName(name);
                        for (User u : list) {
                            if (u.getName().equals(name)) {
                                if (u.getPassword().equals(password)) {
                                    id = u.getId();
                                    break;
                                }
                            }
                        }
                        if (id != -1) {
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            intent.putExtra("mainUserId", id);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameET.getText().toString();
                String password = passwordET.getText().toString();
                int id = -1;

                if(name.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //判断存不存在匹配的user，给出提示
                        List<User> list = db.getUserByName(name);
                        for (User u : list) {
                            if (u.getName().equals(name)) {
                                id = u.getId();
                                break;
                            }
                        }
                        if (id == -1) {
                            User user = new User(name, password);
                            UserDatabase.getUserDAO().insert(user);
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            intent.putExtra("mainUserId", user.getId());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
