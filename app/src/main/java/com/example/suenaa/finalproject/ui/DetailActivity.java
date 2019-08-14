package com.example.suenaa.finalproject.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.model.Comment;
import com.example.suenaa.finalproject.model.Like;
import com.example.suenaa.finalproject.adapter.CommentAdapter;
import com.example.suenaa.finalproject.model.Post;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements CommentAdapter.MyClickListener {
    private int postId, mainUserId;
    private RecyclerView commentRV;
    private List<Comment> comments;
    private CommentAdapter adapter;
    private ImageButton like;
    private PostDatabase mPostDatabase;
    private Button send;
    private EditText com;
    private TextView content;
    private ImageView img;
    private TextView time;
    private TextView comNum;
    private TextView likeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        postId = getIntent().getIntExtra("postId", -1);
        mainUserId = getIntent().getIntExtra("mainUserId", -1);

        PostDatabase.initInstance(this);
        mPostDatabase = PostDatabase.getInstance();
        comments = new ArrayList<>();
        comments = mPostDatabase.getCommentList(postId);

        setViews();

        like.setTag(false);

        setComList();
        setLikeClick();
        setClick();
    }

    public void setViews() {
        commentRV = (RecyclerView)findViewById(R.id.comList);
        like = (ImageButton)findViewById(R.id.likeImage);
        com = (EditText) findViewById(R.id.comText);
        send = (Button) findViewById(R.id.send);
        content = (TextView)findViewById(R.id.content);
        img = (ImageView) findViewById(R.id.image);
        time = (TextView) findViewById(R.id.time);
        comNum = (TextView) findViewById(R.id.comment);
        likeNum = (TextView) findViewById(R.id.like);

        Post current =  PostDatabase.getPostDAO().getById(postId);
        content.setText(current.getContent());
        time.setText(current.getDate());

        if (current.getSrc() == null) {
            img.setImageResource(R.drawable.example_three);
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(current.getSrc(), 0, current.getSrc().length);
            img.setImageBitmap(bitmap);
        }

        comNum.setText(String.valueOf(comments.size()));

        ArrayList<Like> likes = mPostDatabase.getLikeList(postId);
        int count = likes.size();
        likeNum.setText(String.valueOf(count));
    }

    public void setClick() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = com.getText().toString();
                Comment c = new Comment(postId, mainUserId, s);
                comments.add(c);
                PostDatabase.getCommentDAO().insert(c);
                commentRV.getAdapter().notifyItemInserted(comments.size() - 1);
            }
        });
    }

    public void setComList() {
        commentRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(comments, this);

        commentRV.setAdapter(adapter);
    }

    public void setLikeClick() {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Like> likes = mPostDatabase.getLikeList(postId);
                int count = likes.size();

                if((boolean)like.getTag()) {
                    like.setTag(false);
                    like.setImageResource(R.drawable.icon_like_empty);
                    Like like = null;
                    //获取是具体的那个like
                    for (Like l: likes) {
                        if(l.getUser_id().equals(mainUserId)){
                            like = l;
                            break;
                        }
                    }
                    PostDatabase.getLikeDAO().delete(like);
                    count --;
                }
                else {
                    like.setTag(true);
                    like.setImageResource(R.drawable.icon_like);
                    count++;

                    PostDatabase.getLikeDAO().insert(new Like(mainUserId, postId));
                    v.setBackgroundResource(R.drawable.icon_like);
                }
                likeNum.setText(String.valueOf(count));
            }
        });
    }

    /**
     * 接口方法，响应RV的username点击事件
     */
    @Override
    public void clickListener(View v) {
        // 点击之后的操作在这里写
        Intent intent = new Intent();
        intent.setClass(this, LookUserActivity.class);

        ///这里的一块空缺的是填写评论人的id
        //因为不是很懂那个怎么获取相应的User
        //intent.putExtra("userId", )
        //intent.putExtra("mainUserId", mainUserId);
        ///
        startActivity(intent);
        finish();
    }
}
