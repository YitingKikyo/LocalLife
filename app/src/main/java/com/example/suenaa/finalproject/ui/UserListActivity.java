package com.example.suenaa.finalproject.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.database.util.DAO;
import com.example.suenaa.finalproject.model.Follow;
import com.example.suenaa.finalproject.model.User;
import com.example.suenaa.finalproject.adapter.CommentAdapter;
import com.example.suenaa.finalproject.adapter.UserAdapter;
import com.example.suenaa.finalproject.ui.util.BitmapTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView userRV;
    private UserAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Follow> mFollows;
    private int mainUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        int tag = getIntent().getIntExtra("tag", -1);
        Bundle bundle = getIntent().getExtras();

        if(tag == 1){
            mFollows = (ArrayList<Follow>) bundle.getSerializable("followingIdList");
            for (Follow f:mFollows) {
                users.add( UserDatabase.getUserDAO().getById(f.getFollowed_id()));
            }
        }
        else{
            mFollows = (ArrayList<Follow>) bundle.getSerializable("followedIdList");
            for (Follow f:mFollows) {
                users.add( UserDatabase.getUserDAO().getById(f.getFollower_id()));
            }
        }

        mainUserId = getIntent().getIntExtra("mainUserId", -1);
        setUserList();
    }


    void setUserList() {
        adapter = new UserAdapter(users);
        userRV = (RecyclerView)findViewById(R.id.userList);
        userRV.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(UserListActivity.this, LookUserActivity.class);
                intent.putExtra("userId", users.get(position).getId());
                intent.putExtra("mainUserId", mainUserId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        userRV.setAdapter(adapter);
    }

    //我演示一下这样的话如何subscribe数据库的变化
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatabaseChange(DAO.DatabaseChangeEvent event){
        if(!(event.getData() instanceof User)){
            return;
        }
        User user = (User) event.getData();
        if(event.getAction() == DAO.ACTION_INSERT){
            users.add(user);
            userRV.getAdapter().notifyItemInserted(users.size() - 1);
            return;
        }
        int index = users.indexOf(user);
        if(event.getAction() == DAO.ACTION_UPDATE){
            users.set(index, user);
            userRV.getAdapter().notifyItemChanged(index);
        }else {
            users.remove(index);
            userRV.getAdapter().notifyItemRemoved(index);
        }
    }
}
