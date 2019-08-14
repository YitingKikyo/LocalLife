package com.example.suenaa.finalproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.database.UserDatabase;
import com.example.suenaa.finalproject.model.Comment;
import com.example.suenaa.finalproject.R;
import com.example.suenaa.finalproject.model.User;

import java.util.List;

/**
 * Created by user on 2018/1/2.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements View.OnClickListener {
    private OnItemClickListener onItemClickListener;
    private List<Comment> comments;
    private MyClickListener myListener;

    //自定义接口，用于回调点击事件到Activity
    public interface MyClickListener{
        public void clickListener(View v);
    }

    public CommentAdapter(List<Comment> commentList, MyClickListener listener) {
        comments = commentList;
        myListener = listener;
    }

    public void updateUser(List<Comment> commentList) {
        comments = commentList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.com_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Comment c =  comments.get(position);
        int commentUserId = c.getUser_id();
        User u = UserDatabase.getUserDAO().getById(commentUserId);

        holder.username.setText(u.getName());
        holder.content.setText(c.getContent());
        holder.time.setText(c.getDate());

        if(u.getSrc() == null) {
            holder.profile.setImageResource(R.drawable.profile_one);
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(u.getSrc(), 0, u.getSrc().length);
            holder.profile.setImageBitmap(bitmap);
        }

        holder.username.setOnClickListener(this);

        if(onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView username;
        private TextView time;
        private ImageView profile;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
            profile = (ImageView) itemView.findViewById(R.id.profile);
        }
    }

    //响应点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        myListener.clickListener(v);
    }
}
