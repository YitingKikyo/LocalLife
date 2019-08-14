package com.example.suenaa.finalproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suenaa.finalproject.database.PostDatabase;
import com.example.suenaa.finalproject.model.Post;
import com.example.suenaa.finalproject.R;

import java.util.List;


/**
 * Created by user on 2018/1/2.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements View.OnClickListener {
    private OnItemClickListener onItemClickListener;
    private List<Post> posts;
    private LikeClickListener listener;
    private Context mContext;

    //自定义接口，用于回调点击事件到Activity
    public interface LikeClickListener{
        public void clickListener(View v);
    }

    public PostAdapter(List<Post> postList, LikeClickListener listener, Context context) {
        posts = postList;
        this.listener = listener;
        this.mContext = context;
    }

    public void updateRecords(List<Post> postList) {
        posts = postList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Post p = posts.get(position);

        holder.content.setText(p.getContent());
        holder.time.setText(p.getDate());

        PostDatabase.initInstance(mContext);
        PostDatabase db = PostDatabase.getInstance();

        int comC = db.getCommentList(p.getId()).size();
        int likeC = db.getLikeList(p.getId()).size();
        holder.comment.setText(String.valueOf(comC));
        holder.like.setText(String.valueOf(likeC));

        holder.likeImage.setTag(position);
        holder.likeImage.setOnClickListener(this);

        if(p.getPosition() != null) {
            holder.location.setText(p.getPosition());
        }
        else {
            holder.location.setVisibility(View.INVISIBLE);
            holder.locationImg.setVisibility(View.INVISIBLE);
        }

        if(p.getSrc() != null) {
            Bitmap stitchBmp = BitmapFactory.decodeByteArray(p.getSrc(), 0, p.getSrc().length);
            holder.image.setImageBitmap(stitchBmp);
        }
        else {
            holder.image.setImageResource(R.drawable.example_one);
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
        return posts == null ? 0 : posts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView time;
        private TextView location;
        private TextView like;
        private TextView comment;
        private ImageView image;
        private ImageView locationImg;
        private ImageButton likeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            location = (TextView) itemView.findViewById(R.id.location);
            locationImg = (ImageView) itemView.findViewById(R.id.locationImage);
            like = (TextView) itemView.findViewById(R.id.like);
            comment = (TextView) itemView.findViewById(R.id.comment);
            image = (ImageView) itemView.findViewById(R.id.image);
            likeImage = (ImageButton) itemView.findViewById(R.id.likeImage);
        }
    }

    //响应Like点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        listener.clickListener(v);
    }
}
