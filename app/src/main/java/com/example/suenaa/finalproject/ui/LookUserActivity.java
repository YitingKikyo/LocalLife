package com.example.suenaa.finalproject.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.model.Follow;
import com.example.suenaa.finalproject.model.Like;
import com.example.suenaa.finalproject.model.Post;
import com.example.suenaa.finalproject.adapter.PostAdapter;
import com.example.suenaa.finalproject.ui.util.BitmapTool;

import java.util.ArrayList;
import java.util.List;

public class LookUserActivity extends AppCompatActivity implements PostAdapter.LikeClickListener {
    private int mainUserId;
    private int userId;
    private PostDatabase mPostDatabase;
    private RecyclerView postRV;
    private PostAdapter adapter;
    private List<Post> posts;
    private TextView following; //关注列表
    private TextView follower; //被关注列表
    private boolean isClick;
    private FloatingActionButton find;
    private Button followButton;
    private TextView mName;
    private ArrayList<Follow> followingList, followedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        PostDatabase.initInstance(this);
        mPostDatabase = PostDatabase.getInstance();


        userId = getIntent().getIntExtra("userId", -1);
        mainUserId = getIntent().getIntExtra("mainUserId", -1);

        //处置赋值问题通main
        isClick = false;
        posts = mPostDatabase.getPosts(userId);
        setViews();
        setClick();
        setPostList();
    }

    public void setViews() {
        postRV = (RecyclerView)findViewById(R.id.record);
        following = (TextView) findViewById(R.id.follow);
        follower = (TextView) findViewById(R.id.follower);
        find = (FloatingActionButton) findViewById(R.id.find);
        followButton = (Button) findViewById(R.id.foButton);
        mName = (TextView)findViewById(R.id.username);

        mName.setText(UserDatabase.getUserDAO().getById(userId).getName());

        followingList = mPostDatabase.getFollowingList(userId);
        following.setText(String.valueOf(followingList.size()));
        followedList = mPostDatabase.getFollowedList(userId);
        follower.setText(String.valueOf(followedList.size()));

        ArrayList<Follow> mainUserFollowingList = mPostDatabase.getFollowingList(mainUserId);
        for(Follow l : mainUserFollowingList){
            if(l.getFollower_id().equals(mainUserId) && l.getFollowed_id() == userId){
                followButton.setText("已关注");
                break;
            }
        }

    }

    /////////
    /////////
    /////////
    /////////

    //following, folloer click
    public void setClick() {
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Follow> followingList = mPostDatabase.getFollowingList(userId);

                Intent intent = new Intent();
                intent.setClass(LookUserActivity.this, UserListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followingIdList", followingList);
                intent.putExtras(bundle);
                intent.putExtra("tag", 1);
                intent.putExtra("mainUserId",  mainUserId);
                startActivity(intent);
            }
        });

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Follow> followedList = mPostDatabase.getFollowedList(userId);

                Intent intent = new Intent();
                intent.setClass(LookUserActivity.this, UserListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followedIdList", followedList);
                intent.putExtras(bundle);
                intent.putExtra("tag", 2);
                intent.putExtra("mainUserId",  mainUserId);
                startActivity(intent);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LookUserActivity.this, FindActivity.class);
                intent.putExtra("mainUserId", mainUserId);
                startActivity(intent);
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(followButton.getText().equals("关注")) {
                    if (mainUserId == userId) {
                        Toast.makeText(LookUserActivity.this, "无法关注自己", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Follow ff = new Follow(mainUserId, userId);
                        PostDatabase.getFollowDAO().insert(ff);
                        followButton.setText("已关注");
                    }
                }
                else{
                    //取关
                    Follow tmp = null;
                    ArrayList<Follow> mainUserFollowingList = mPostDatabase.getFollowingList(mainUserId);
                    for(Follow l : mainUserFollowingList){
                        if(l.getFollowed_id() == userId && l.getFollower_id() == mainUserId){
                            tmp = l;
                            break;
                        }
                    }
                    PostDatabase.getFollowDAO().delete(tmp);
                }
            }
        });
    }

    public void setPostList() {
        postRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(posts, this, LookUserActivity.this);

        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(LookUserActivity.this, DetailActivity.class);
                intent.putExtra("postId", posts.get(position).getId());
                intent.putExtra("mainUserId", mainUserId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        postRV.setAdapter(adapter);
    }

    // 心心点击事件
    public void clickListener(View v) {
        ///
        ///这里的click按照Mainactivity改
        ///
        ////
        ////////////////
        int pos = (int)v.getTag();
        int postId = posts.get(pos).getId();
        ArrayList<Like> likes = mPostDatabase.getLikeList(postId);
        int count = likes.size();

        if (isClick) {
            isClick = false;
            count--;
            Like like = null;
            //获取是具体的那个like
            for (Like l: likes) {
                if(l.getUser_id().equals(userId)){
                    like = l;
                    break;
                }
            }
            PostDatabase.getLikeDAO().delete(like);
            v.setBackgroundResource(R.drawable.icon_like_empty);
        }
        else {
            isClick = true;
            count++;

            PostDatabase.getLikeDAO().insert(new Like(userId, postId));
            v.setBackgroundResource(R.drawable.icon_like);
        }
        adapter.updateRecords(posts);

    }
}
