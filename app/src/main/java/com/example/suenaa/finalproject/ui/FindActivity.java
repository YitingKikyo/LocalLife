package com.example.suenaa.finalproject.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.adapter.CommentAdapter;
import com.example.suenaa.finalproject.adapter.UserAdapter;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends AppCompatActivity {
    private Button find;
    private EditText key;
    private RecyclerView resultList;
    private UserAdapter adapter;
    private List<User> users;
    private int mainUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        mainUserId = getIntent().getIntExtra("mainUserId", -1);

        find = (Button) findViewById(R.id.find);
        key = (EditText) findViewById(R.id.keyword);
        resultList = (RecyclerView)findViewById(R.id.resultList);
        users = new ArrayList<>();

        setUserList();
        setClick();
    }

    public void setClick() {
        UserDatabase.initInstance(FindActivity.this);
        final UserDatabase db = UserDatabase.getInstance();
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kewWord = key.getText().toString();
                List<User> list = db.getUserByName(kewWord);
                for (User u : list) {
                    users.add(u);
                }
                adapter.updateUser(users);
            }
        });
    }

    public void setUserList() {
        adapter = new UserAdapter(users);
        resultList.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(FindActivity.this, LookUserActivity.class);
                intent.putExtra("userId", users.get(position).getId());
                intent.putExtra("mainUserId", mainUserId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        resultList.setAdapter(adapter);
    }
}
