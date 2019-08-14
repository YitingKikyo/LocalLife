package com.example.suenaa.finalproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suenaa.finalproject.model.User;
import com.example.suenaa.finalproject.R;

import java.util.List;

/**
 * Created by user on 2018/1/2.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private CommentAdapter.OnItemClickListener onItemClickListener;
    private List<User> users;

    public UserAdapter(List<User> userList) {
        users = userList;
    }

    public void updateUser(List<User> userList) {
        users = userList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_detail, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, final int position) {
        User u = users.get(position);
        holder.username.setText(u.getName());
        ///////////////////////////////////////////////
        ///////////需修改
        byte[] pro = u.getSrc();
        if (pro != null) {
            Bitmap stitchBmp = BitmapFactory.decodeByteArray(pro, 0, pro.length);
            holder.profile.setImageBitmap(stitchBmp);
            //holder.profile.setImageResource(R.drawable.example_three);
        }
        else {
            holder.profile.setImageResource(R.drawable.profile_one);
        }

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
        return users == null ? 0 : users.size();
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView profile;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            profile = (ImageView) itemView.findViewById(R.id.profile);
        }
    }
}
