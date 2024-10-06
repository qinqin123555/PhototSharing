package com.example.phototsharing.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeChildDetailFirstCommentAdapter;
import com.example.phototsharing.adapter.HomeChildDetailImageAdapter;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.ShareDetailBean;
import com.example.phototsharing.net.CommentCallback;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.ShareDetailCallback;
import com.example.phototsharing.net.TrueOrFalseCallback;
import com.example.phototsharing.utilis.DialogWithKeyboard;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFindDetailActivity extends AppCompatActivity {
    private long shareId;
    private String username;
    private String avatar;
    private long myUserId;
    private String myUserName;


    private HomeChildDetailImageAdapter homeChildDetailImageAdapter;
    private HomeChildDetailFirstCommentAdapter homeChildDetailFirstCommentAdapter;
    private Context myContext;

    private ImageView backImg;
    private CircleImageView profilePicture;
    private TextView userName;
    private Button focusBtn;

//    private ScrollView homeChildDetailScrollView;
    private RecyclerView homeChildDetailImageRecyclerView;
    private TextView homeChildDetailTitle;
    private TextView homeChildDetailContent;
    private TextView homeChildDetailTotalCommentNum;
    private RecyclerView commentView;

    private EditText homeChildDetailCommentEditText;
    private ImageView icLike;
    private TextView likeNum;
    private ImageView icCollect;
    private TextView collectNum;

    private boolean hasFocus;
    private boolean hasLike;
    private boolean hasCollect;
    private long focusUserId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_find_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


//        接收HomeChildFragment传来的shareId
        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {

            shareId = receivedBundle.getLong("shareId");
            username = receivedBundle.getString("username");
            avatar = receivedBundle.getString("avatar");
        }

        //初始化一些参数
        myUserId = 1838948060948992000L;
        myUserName = "DM";
        myContext = getApplicationContext();
        homeChildDetailImageAdapter = new HomeChildDetailImageAdapter();
//        homeChildDetailScrollView = findViewById(R.id.home_child_detail_scroll_view);
        homeChildDetailImageRecyclerView = findViewById(R.id.home_child_detail_image_recycler_view);
        backImg = findViewById(R.id.iv_back);
        profilePicture = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.username);
        focusBtn = findViewById(R.id.focus);
        homeChildDetailTitle = findViewById(R.id.tv_home_child_detail_title);
        homeChildDetailContent = findViewById(R.id.tv_home_child_detail_content);
        homeChildDetailTotalCommentNum = findViewById(R.id.tv_home_child_total_comment_num);
        commentView = findViewById(R.id.home_child_detail_comment_view);
        homeChildDetailCommentEditText = findViewById(R.id.home_child_detail_comment_edit_text);
        icLike = findViewById(R.id.iv_home_child_detail_like);
        likeNum = findViewById(R.id.tv_home_child_detail_like_number);
        icCollect = findViewById(R.id.iv_home_child_detail_collect);
        collectNum = findViewById(R.id.tv_home_child_detail_collect_number);

//        获取该分享的详细信息，并初始化页面
        MyRequest.getShareDetailData(shareId, myUserId,new ShareDetailCallback() {
            @Override
            public void onSuccess(ShareDetailBean shareDetailBean) {

//              是否关注、点赞、收藏
                hasCollect = shareDetailBean.getData().getHasCollect();
                hasFocus = shareDetailBean.getData().getHasFocus();
                hasLike = shareDetailBean.getData().getHasLike();
                focusUserId = shareDetailBean.getData().getPUserId();

                initView(shareDetailBean);

                //初始化一级评论区
                MyRequest.getFirstCommentData(shareId, new CommentCallback() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(CommentBean commentBean) {
                        homeChildDetailTotalCommentNum.setText(String.format("共%d条评论", commentBean.getData().getTotal()));

                        Log.e("TAG","评论数量"+String.valueOf(commentBean.getData().getTotal()));
                        homeChildDetailFirstCommentAdapter = new HomeChildDetailFirstCommentAdapter(commentBean,myContext);
                        homeChildDetailFirstCommentAdapter.notifyItemInserted(0);
                        Log.e("TAG","评论数量"+String.valueOf(commentBean.getData().getTotal()));

                        commentView.setAdapter(homeChildDetailFirstCommentAdapter);
                        Log.e("TAG","评论数量"+String.valueOf(commentBean.getData().getTotal()));

                        commentView.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.VERTICAL,false));
                        commentView.addItemDecoration(new DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL));

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("TAG","无评论");

                    }
                });
            }
            @Override
            public void onFailure(Throwable throwable) {

            }
        });


//        backImg的事件监听，返回上一个Activity
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        关注按钮的点击事件监听
        setFocusBtnClickListener(focusBtn, myUserId);

//        点赞的事件监听
        setIcLikeClickListener(icLike,likeNum,shareId, myUserId);

//        收藏的事件监听
        setIcCollectClickListener(icCollect,collectNum,shareId, myUserId);
        setCommentEditTextOnClickListener(homeChildDetailCommentEditText);

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initView(ShareDetailBean shareDetailBean){
        homeChildDetailImageAdapter.changeAdapter(myContext,shareDetailBean.getData().getImageUrlList());
        homeChildDetailImageRecyclerView.setAdapter(homeChildDetailImageAdapter);
        homeChildDetailImageRecyclerView.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false));

        //名字
        if (shareDetailBean.getData().getUsername() != null) {
            userName.setText(shareDetailBean.getData().getUsername());
        } else {
            userName.setText(username);
        }
//        标题
        if (shareDetailBean.getData().getTitle() != null) {
            homeChildDetailTitle.setText(shareDetailBean.getData().getTitle());
        } else {
            homeChildDetailTitle.setText("标题");
        }
//        内容
        if (shareDetailBean.getData().getContent() != null) {
            homeChildDetailContent.setText(shareDetailBean.getData().getContent());
        } else {
            homeChildDetailContent.setText("内容");
        }
//        点赞数量
        if (shareDetailBean.getData().getLikeNum() != null) {
            likeNum.setText(String.format("%d",shareDetailBean.getData().getLikeNum()));
        } else {
            likeNum.setText("0");
        }
//        收藏数量
        if (shareDetailBean.getData().getCollectNum() != null) {
            collectNum.setText(String.format("%d",shareDetailBean.getData().getCollectNum()));
        } else {
            collectNum.setText("0");
        }
//        头像
        if (shareDetailBean.getData().getAvatar() != null) {
            Glide.with(myContext)
                    .load(shareDetailBean.getData().getAvatar())
                    .into(profilePicture);
        } else {
            Glide.with(myContext)
                    .load(avatar)
                    .into(profilePicture);
        }

        if (hasFocus) {
            focusBtn.setText("已关注");
        } else {
            focusBtn.setText("关注");
        }

        icLike.setSelected(hasLike);

        icCollect.setSelected(hasCollect);


    }

    private void setCommentEditTextOnClickListener (EditText commentEditText) {

        commentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWithKeyboard dialogWithKeyboard = new DialogWithKeyboard(new DialogWithKeyboard.OnSendClickListener() {
                    @Override
                    public void onSendClick(String content) {
                        Log.d("TAG","点击发送评论");
                        Log.d("content",content);

                        if (!content.isEmpty()){
                            MyRequest.addFirstComment(myContext,content,shareId,myUserId,myUserName);
                        }
                    }
                });
                dialogWithKeyboard.showDialogWithKeyboard(HomeFindDetailActivity.this);

            }
        });
    }

    //关注按钮点击事件监听
    private void setFocusBtnClickListener(Button button,long userId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理点击事件的代码
                if (hasFocus) {
                    MyRequest.chancelFocusRequest(focusUserId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            button.setText("关注");
                            Toast.makeText(myContext, "已取消关注", Toast.LENGTH_SHORT).show();
                            hasFocus = b;
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    MyRequest.setFocusRequest(focusUserId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            button.setText("已关注");
                            hasFocus = b;
                            Toast.makeText(myContext, "已关注", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



//    点赞的点击事件监听
    private void setIcLikeClickListener(ImageView like,TextView likeNumber,long shareId,long userId) {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasLike) {
                    MyRequest.cancelLikeRequest(shareId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            like.setSelected(b);
                            hasLike = b;
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = likeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount--; // 对长整型值进行加法运算
                            likeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已取消点赞", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    MyRequest.setLikeRequest(shareId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            like.setSelected(b);
                            hasLike = b;
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = likeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount++; // 对长整型值进行加法运算
                            likeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已点赞", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



//    收藏的的点击事件监听
    private void setIcCollectClickListener(ImageView collect,TextView collectNumber,long shareId,long userId) {
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCollect) {
                    MyRequest.cancelCollectRequest(shareId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            collect.setSelected(b);
                            hasCollect = b;
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = collectNumber.getText().toString(); // 获取 TextView 中的文本
                            long collectCount = Long.parseLong(currentText); // 将文本转换为长整型
                            collectCount--; // 对长整型值进行加法运算
                            collectNumber.setText(String.valueOf(collectCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext,"已取消收藏",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    MyRequest.setCollectRequest(shareId, userId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            collect.setSelected(b);
                            hasCollect = b;
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = collectNumber.getText().toString(); // 获取 TextView 中的文本
                            long collectCount = Long.parseLong(currentText); // 将文本转换为长整型
                            collectCount++; // 对长整型值进行加法运算
                            collectNumber.setText(String.valueOf(collectCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext,"已加入收藏列表",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext,"无法加入收藏列表",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}