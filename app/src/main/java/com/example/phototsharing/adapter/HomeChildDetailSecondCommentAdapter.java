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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.GetUserInfoCallback;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.TrueOrFalseCallback;
import com.example.phototsharing.utilis.DialogWithKeyboard;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeChildDetailSecondCommentAdapter extends RecyclerView.Adapter<HomeChildDetailSecondCommentAdapter.ViewHolder> {
    private CommentBean secondComment;
    private Context myContext;
    private long parentCommentId;
    private long parentCommentUserId;
    private long myUserId;
    private String myUserName;

    public HomeChildDetailSecondCommentAdapter(Context myContext){
        this.myContext = myContext;
    }

    public HomeChildDetailSecondCommentAdapter(CommentBean secondComment, Context myContext,long ParentCommentId,long ParentCommentUserId,long myUserId,String myUserName) {
        this.secondComment = secondComment;
        this.myContext = myContext;
        this.parentCommentId = ParentCommentId;
        this.parentCommentUserId = ParentCommentUserId;
        this.myUserId = myUserId;
        this.myUserName = myUserName;
    }


    @NonNull
    @Override
    public HomeChildDetailSecondCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext)
                .inflate(R.layout.home_child_detail_second_comment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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


//        为一级评论设置点击监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                long replyCommentId = secondComment.getData().getRecords().get(position).getid();
                long replyCommentUserId = secondComment.getData().getRecords().get(position).getPUserId();
                long shareId = secondComment.getData().getRecords().get(position).getShareId();


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
                            });                        }
                    }
                });
                dialogWithKeyboard.showDialogWithKeyboard(myContext);

                Log.d("TAG","点击评论");
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
