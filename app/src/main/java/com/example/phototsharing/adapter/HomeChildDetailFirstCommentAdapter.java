package com.example.phototsharing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototsharing.R;
import com.example.phototsharing.entity.CommentBean;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeChildDetailFirstCommentAdapter extends RecyclerView.Adapter<HomeChildDetailFirstCommentAdapter.viewHolder> {
    private Context myContext;
    private CommentBean myFirstCommentBean;

    public HomeChildDetailFirstCommentAdapter(Context context){
        this.myContext = context;
    }

    public HomeChildDetailFirstCommentAdapter(CommentBean commentBean,Context context){
        this.myFirstCommentBean = commentBean;
        this.myContext = context;
    }

    public void changeAdapter(CommentBean commentBean,Context context){
        this.myFirstCommentBean = commentBean;
        this.myContext = context;
    }




    @NonNull
    @Override
    public HomeChildDetailFirstCommentAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_child_detail_comment_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChildDetailFirstCommentAdapter.viewHolder holder, int position) {
        holder.firstCommentUserName.setText(myFirstCommentBean.getData().getRecords().get(position).getUserName());
        holder.firstCommentContent.setText(myFirstCommentBean.getData().getRecords().get(position).getContent());
        holder.firstCommentTime.setText(myFirstCommentBean.getData().getRecords().get(position).getCreateTime());
    }

    @Override
    public int getItemCount() {
        if (myFirstCommentBean == null){
            return 0;
        }
        return myFirstCommentBean.getData().getRecords().size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private CircleImageView firstCommentAvatar;
        private TextView firstCommentUserName;
        private TextView firstCommentContent;
        private TextView firstCommentTime;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            firstCommentAvatar = itemView.findViewById(R.id.iv_first_comment_avatar);
            firstCommentUserName = itemView.findViewById(R.id.tv_first_comment_username);
            firstCommentContent = itemView.findViewById(R.id.tv_first_comment_content);
            firstCommentTime = itemView.findViewById(R.id.tv_first_comment_time);
        }
    }
}
