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
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.GetUserInfoCallback;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeChildDetailSecondCommentAdapter extends RecyclerView.Adapter<HomeChildDetailSecondCommentAdapter.ViewHolder> {
    private CommentBean secondComment;
    private Context myContext;

    public HomeChildDetailSecondCommentAdapter(Context myContext){
        this.myContext = myContext;
    }

    public HomeChildDetailSecondCommentAdapter(CommentBean secondComment, Context myContext) {
        this.secondComment = secondComment;
        this.myContext = myContext;
    }
    public void changeAdapter(CommentBean secondComment,Context myContext) {
        this.secondComment = secondComment;
        this.myContext = myContext;
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
        Log.d("二级评论","进入二级评论Adapter");

        holder.secondCommentUserName.setText(secondComment.getData().getRecords().get(position).getUserName());
        holder.secondCommentContent.setText(secondComment.getData().getRecords().get(position).getContent());
        holder.secondCommentTime.setText(secondComment.getData().getRecords().get(position).getCreateTime());

        String username = secondComment.getData().getRecords().get(position).getUserName();

        //        获取该评论者的头像
        MyRequest.getUserByName(username, new GetUserInfoCallback() {
            @Override
            public void onSuccess(PersonBean personBean) {
                Glide.with(myContext)
                        .load(personBean.getData().getAvatar())
                        .into(holder.secondCommentAvatar);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("TAG","无头像");
            }
        });



    }

    @Override
    public int getItemCount() {
        if (secondComment == null){
            return 0;
        }
        return secondComment.getData().getRecords().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView secondCommentAvatar;
        public TextView secondCommentUserName;
        public TextView secondCommentContent;
        public TextView secondCommentTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            secondCommentAvatar = itemView.findViewById(R.id.iv_second_comment_avatar);
            secondCommentUserName = itemView.findViewById(R.id.tv_second_comment_username);
            secondCommentContent = itemView.findViewById(R.id.tv_second_comment_content);
            secondCommentTime = itemView.findViewById(R.id.tv_second_comment_time);
        }
    }
}
