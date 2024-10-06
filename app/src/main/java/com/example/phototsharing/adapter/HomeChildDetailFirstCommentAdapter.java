package com.example.phototsharing.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.GetUserInfoCallback;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.CommentCallback;

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
        String username = myFirstCommentBean.getData().getRecords().get(position).getUserName();

        long shareId = myFirstCommentBean.getData().getRecords().get(position).getShareId();
        long firstCommentId = myFirstCommentBean.getData().getRecords().get(position).getid();

//        获取该评论者的头像
        MyRequest.getUserByName(username, new GetUserInfoCallback() {
            @Override
            public void onSuccess(PersonBean personBean) {
                Glide.with(myContext)
                        .load(personBean.getData().getAvatar())
                        .into(holder.firstCommentAvatar);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("TAG","无头像");
            }
        });

//        加载二级评论
        MyRequest.getSecondCommentData(shareId, firstCommentId, new CommentCallback() {
            @Override
            public void onSuccess(CommentBean commentBean) {
                HomeChildDetailSecondCommentAdapter homeChildDetailSecondCommentAdapter = new HomeChildDetailSecondCommentAdapter(commentBean,myContext);
                holder.secondCommentView.setAdapter(homeChildDetailSecondCommentAdapter);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG","没有二级评论");
            }
        });

//       为一级评论设置点击监听
 /*       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                String content = showDialogWithKeyboard(context);
                long parentCommentId = holder.itemView.;
                long parentCommentUserId;
                long replyCommentId;
                long replyCommentUserId;
                long shareId;
                long userId;
                String userName;

                MyRequest.addSecondComment(newCommentContent,);

               Log.d("TAG","点击评论");
            }
        });*/


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
        private RecyclerView secondCommentView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            firstCommentAvatar = itemView.findViewById(R.id.iv_first_comment_avatar);
            firstCommentUserName = itemView.findViewById(R.id.tv_first_comment_username);
            firstCommentContent = itemView.findViewById(R.id.tv_first_comment_content);
            firstCommentTime = itemView.findViewById(R.id.tv_first_comment_time);
            secondCommentView = itemView.findViewById(R.id.home_child_detail_second_comment_view);
        }
    }








}
