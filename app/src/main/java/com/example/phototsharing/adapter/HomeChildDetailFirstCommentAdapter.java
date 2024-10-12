package com.example.phototsharing.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.activity.HomeFindDetailActivity;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.GetUserInfoCallback;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.CommentCallback;
import com.example.phototsharing.net.TrueOrFalseCallback;
import com.example.phototsharing.utilis.DialogWithKeyboard;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeChildDetailFirstCommentAdapter extends RecyclerView.Adapter<HomeChildDetailFirstCommentAdapter.viewHolder> {
    private Context myContext;
    private CommentBean myFirstCommentBean;
    private ArrayList<CommentBean> secondCommentBeanList;
    private long myUserId;
    private String myUserName;

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
    public void onBindViewHolder(@NonNull HomeChildDetailFirstCommentAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {


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
                long parentCommentId = myFirstCommentBean.getData().getRecords().get(position).getid();
                long parentCommentUserId = myFirstCommentBean.getData().getRecords().get(position).getPUserId();
                HomeChildDetailSecondCommentAdapter homeChildDetailSecondCommentAdapter = new HomeChildDetailSecondCommentAdapter(commentBean,myContext,parentCommentId,parentCommentUserId,myUserId,myUserName);
                holder.secondCommentView.setAdapter(homeChildDetailSecondCommentAdapter);
                holder.secondCommentView.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.VERTICAL,false));
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d("获取二级评论","没有二级评论");
            }
        });



//       为一级评论设置点击监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                long parentCommentId = myFirstCommentBean.getData().getRecords().get(position).getid();
                long parentCommentUserId = myFirstCommentBean.getData().getRecords().get(position).getPUserId();
                long replyCommentId = myFirstCommentBean.getData().getRecords().get(position).getid();
                long replyCommentUserId = myFirstCommentBean.getData().getRecords().get(position).getPUserId();
                long shareId = myFirstCommentBean.getData().getRecords().get(position).getShareId();


                DialogWithKeyboard dialogWithKeyboard = new DialogWithKeyboard(new DialogWithKeyboard.OnSendClickListener() {
                    @Override
                    public void onSendClick(String content) {
                        Log.d("TAG","点击发送评论");
                        Log.d("content",content);

                        if (!content.isEmpty()){
                            MyRequest.addSecondComment(content, parentCommentId, parentCommentUserId, replyCommentId, replyCommentUserId, shareId, myUserId, myUserName, new TrueOrFalseCallback() {
                                @Override
                                public void onSuccess(Boolean b) {
                                    Toast.makeText(myContext,"成功回复",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Toast.makeText(myContext,String.valueOf(throwable),Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
                dialogWithKeyboard.showDialogWithKeyboard(myContext);

                Log.d("TAG","点击评论");
            }
        });


    }



        @Override
    public int getItemCount() {
        if (myFirstCommentBean == null){
            return 0;
        }
        return myFirstCommentBean.getData().getRecords().size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public CircleImageView firstCommentAvatar;
        public TextView firstCommentUserName;
        public TextView firstCommentContent;
        public TextView firstCommentTime;
        public RecyclerView secondCommentView;

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
