package com.example.suenaa.finalproject.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.model.Follow;
import com.example.suenaa.finalproject.model.Like;
import com.example.suenaa.finalproject.model.Post;
import com.example.suenaa.finalproject.adapter.PostAdapter;
import com.example.suenaa.finalproject.model.User;
import com.example.suenaa.finalproject.ui.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostAdapter.LikeClickListener {
    private static final int RESULT = 1;
    private int userId;
    private PostDatabase mPostDatabase;
    private RecyclerView postRV;
    private PostAdapter adapter;
    private List<Post> posts = new ArrayList<>();
    private FloatingActionButton add, find;
    private TextView following; //关注列表
    private TextView follower; //被关注列表
    private TextView username;
    private ImageView profile;
    boolean isClick;
    private byte[] byteArray;
    private ArrayList<Follow> followingList;
    private ArrayList<Follow> followedList;

    //这里还要获取following、followed的数量
    //获取下面Post动态评论的数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PostDatabase.initInstance(this);
        mPostDatabase = PostDatabase.getInstance();

        userId = getIntent().getIntExtra("mainUserId", -1);
        following = (TextView) findViewById(R.id.follow);
        follower = (TextView) findViewById(R.id.follower);

        followingList = mPostDatabase.getFollowingList(userId);
        followedList = mPostDatabase.getFollowedList(userId);

        //之前判断isclick错误
        //isClick = false;

        setViews();
        setClick();
        setAdd();
        setPostList();
    }


    //更改数量
    @Override
    public void onResume(){
//        followingList = mPostDatabase.getFollowingList(userId);
//        followedList = mPostDatabase.getFollowedList(userId);
//        follower.setText(followedList.size());
//        following.setText(followingList.size());
        super.onResume();
    }

    public void setViews() {
        postRV = (RecyclerView) findViewById(R.id.record);
        add = (FloatingActionButton) findViewById(R.id.add);
        find = (FloatingActionButton) findViewById(R.id.find);
        username = (TextView) findViewById(R.id.username);
        profile = (ImageView) findViewById(R.id.profile);

        username.setText(UserDatabase.getUserDAO().getById(userId).getName());

        followingList = mPostDatabase.getFollowingList(userId);
        following.setText(String.valueOf(followingList.size()));
        followedList = mPostDatabase.getFollowedList(userId);
        follower.setText(String.valueOf(followedList.size()));
    }


    public void setAdd() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                final View viewDialog = factor.inflate(R.layout.edit_dialog, null);
                ImageButton camera = (ImageButton) viewDialog.findViewById(R.id.camera);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //builder.setTitle();
                builder.setView(viewDialog);
                builder.setPositiveButton("发布", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText cont = (EditText)viewDialog.findViewById(R.id.content);
                        String c = cont.getText().toString();
                        Post p = new Post(userId, c, "");
                        p.setSrc(byteArray);
                        PostDatabase.getPostDAO().insert(p);
                        posts.add(p);
                        postRV.getAdapter().notifyItemInserted(posts.size() - 1);


                        //////
                        //////更改信息的数量//
                        //数量获取方法
                        //mPostDatabase.getCommentList(p.getId()).size();
                        //还有在一开始进入这个activity也要初始化这个数量

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    //following, folloer click
    public void setClick() {
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UserListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followingIdList", followingList);
                intent.putExtras(bundle);
                intent.putExtra("tag", 1);
                intent.putExtra("mainUserId", userId);
                startActivity(intent);
            }
        });

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UserListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followedIdList", followedList);
                intent.putExtras(bundle);
                intent.putExtra("tag", 2);
                intent.putExtra("mainUserId", userId);
                startActivity(intent);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, FindActivity.class);
                intent.putExtra("mainUserId", userId);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT);

                User u = UserDatabase.getUserDAO().getById(userId);
                u.setSrc(byteArray);
                UserDatabase.getUserDAO().update(u);
                Bitmap stitchBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                profile.setImageBitmap(stitchBmp);
            }
        });
    }

    public void setPostList() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postRV.setLayoutManager(layoutManager);
        posts = mPostDatabase.getPosts(userId);
        adapter = new PostAdapter(posts, this, MainActivity.this);


        //点击事件
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("mainUserId", userId);
                intent.putExtra("postId", posts.get(position).getId());
                startActivity(intent);
            }

            //暂定长按删除，测试用
            @Override
            public void onLongClick(int position) {
                Post p = posts.get(position);
                posts.remove(position);
                adapter.notifyItemRemoved(position);
                PostDatabase.getPostDAO().delete(p);
            }
        });
        postRV.setAdapter(adapter);
    }

    // 心心点击事件
    public void clickListener(View v) {
        int pos = (int)v.getTag();

        int postId = posts.get(pos).getId();
        ArrayList<Like> likes = mPostDatabase.getLikeList(postId);
        int count = likes.size();

        for(Like l : likes){
            if(l.getUser_id().equals(userId)){
                //说明之前已经like过
                //然后这里的isClick在你赋初值的时候是错误的
                //按照这个你重新再赋初值，我没有一开始就设定颜色
                isClick = true;
                break;
            }
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT && resultCode == RESULT_OK && data != null){

            //从图库中选择图片
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //设置图片并将图片的bitmap格式转换成byte[]存储起来
            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            //photo.setImageBitmap(bm);
             byteArray = BitmapUtil.toByteArray(bm);
        }
    }

}
