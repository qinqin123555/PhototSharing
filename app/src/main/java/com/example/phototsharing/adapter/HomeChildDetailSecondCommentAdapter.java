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

public class HomeChildDetailSecondCommentAdapter extends RecyclerView.Adapter<HomeChildDetailSecondCommentAdapter.ViewHolder> {
    private CommentBean secondComment;
    private Context myContext;
    private HomeChildDetailSecondCommentAdapter homeChildDetailSecondCommentAdapter;

    public HomeChildDetailSecondCommentAdapter(){}

    public HomeChildDetailSecondCommentAdapter(CommentBean secondComment, Context myContext, HomeChildDetailSecondCommentAdapter secondCommentAdapter) {
        this.secondComment = secondComment;
        this.myContext = myContext;
        this.homeChildDetailSecondCommentAdapter = secondCommentAdapter;
    }
    public void changeAdapter(CommentBean secondComment,Context myContext, HomeChildDetailSecondCommentAdapter secondCommentAdapter) {
        this.secondComment = secondComment;
        this.myContext = myContext;
        this.homeChildDetailSecondCommentAdapter = secondCommentAdapter;
    }



    @NonNull
    @Override
    public HomeChildDetailSecondCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext)
                .inflate(R.layout.home_child_detail_second_comment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.secondCommentUserName.setText(secondComment.getData().getRecords().get(position).getUserName());
        holder.secondCommentContent.setText(secondComment.getData().getRecords().get(position).getContent());
        holder.secondCommentTime.setText(secondComment.getData().getRecords().get(position).getCreateTime());
/*
        holder.secondCommentRecyclerView = new RecyclerView(myContext);
        holder.secondCommentRecyclerView.setAdapter();
*/

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView secondCommentAvatar;
        private TextView secondCommentUserName;
        private TextView secondCommentContent;
        private TextView secondCommentTime;
        private RecyclerView secondCommentRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            secondCommentAvatar = itemView.findViewById(R.id.iv_second_comment_avatar);
            secondCommentUserName = itemView.findViewById(R.id.tv_second_comment_username);
            secondCommentContent = itemView.findViewById(R.id.tv_second_comment_content);
            secondCommentTime = itemView.findViewById(R.id.tv_second_comment_time);
            secondCommentRecyclerView = itemView.findViewById(R.id.home_child_detail_second_comment_view);
        }
    }
}
