package com.example.phototsharing.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeChildDetailActivity extends AppCompatActivity {
    private long shareId;
    private String username;
    private String avatar;


    private ShareDetailBean myShareDetailBean;
    private CommentBean myFirstComment;
    private HomeChildDetailImageAdapter homeChildDetailImageAdapter;
    private HomeChildDetailFirstCommentAdapter homeChildDetailFirstCommentAdapter;
    private Context myContext;

    private ImageView backImg;
    private CircleImageView profilePicture;
    private TextView userName;
    private Button focusBtn;

    private ScrollView homeChildDetailScrollView;
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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_child_detail);
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
        myContext = getApplicationContext();
        myShareDetailBean = new ShareDetailBean();
        myFirstComment = new CommentBean();
        homeChildDetailImageAdapter = new HomeChildDetailImageAdapter();
        homeChildDetailScrollView = findViewById(R.id.home_child_detail_scroll_view);
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

        loadShareDetailData(shareId, new ShareDetailCallback() {
            @Override
            public void onSuccess(ShareDetailBean shareDetailBean) {
                initView(shareDetailBean);

                //初始化一级评论区
                loadFirstCommentData(shareId, new FirstCommentCallback() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(CommentBean firstcommentBean) {
                        homeChildDetailTotalCommentNum.setText(String.format("共%d条评论",firstcommentBean.getData().getTotal()));

                        Log.e("TAG","评论数量"+String.valueOf(firstcommentBean.getData().getTotal()));
                        homeChildDetailFirstCommentAdapter = new HomeChildDetailFirstCommentAdapter(firstcommentBean,myContext);
                        Log.e("TAG","评论数量"+String.valueOf(firstcommentBean.getData().getTotal()));

                        commentView.setAdapter(homeChildDetailFirstCommentAdapter);
                        Log.e("TAG","评论数量"+String.valueOf(firstcommentBean.getData().getTotal()));

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
    }




//    定义获取分享详情的回调接口
public interface ShareDetailCallback {
    void onSuccess(ShareDetailBean shareDetailBean);
    void onFailure(Throwable throwable);
}

//获取分享详情
public void loadShareDetailData(long shareId, ShareDetailCallback callback) {
    ApiInterface myApiInterface = MyRequest.request(); // 假设request()方法返回你的ApiInterface实例
    Call<ShareDetailBean> call = myApiInterface.getFindDetailInfo(MyHeaders.getAppId(), MyHeaders.getAppSecret(), shareId, 1838948060948992000L);
    call.enqueue(new Callback<ShareDetailBean>() {
        @Override
        public void onResponse(@NonNull Call<ShareDetailBean> call, @NonNull Response<ShareDetailBean> response) {
            if (response.isSuccessful()) {
                ShareDetailBean shareDetailBean = response.body();
                if (shareDetailBean != null) {
                    callback.onSuccess(shareDetailBean);
                } else {
                    // 可以选择在这里处理空响应体的情况，或者通过回调通知调用者
                    callback.onFailure(new NullPointerException("Response body is null"));
                }
            } else {
                // 可以从response.errorBody()中获取更多错误信息，但这里为了简单起见只传递了Throwable
                callback.onFailure(new Exception("HTTP error: " + response.code()));
            }
        }

        @Override
        public void onFailure(@NonNull Call<ShareDetailBean> call, @NonNull Throwable t) {
            callback.onFailure(t);
        }
    });
}

//定义获取一级评论的回调接口
public interface FirstCommentCallback {
    void onSuccess(CommentBean firstcommentBean);
    void onFailure(Throwable throwable);
}


//    获取一级评论
// 注意：这里假设loadSecondCommentData已经被修改为接受一个回调接口
private void loadFirstCommentData(long shareId, FirstCommentCallback callback) {
    ApiInterface myApi = MyRequest.request();
    Call<CommentBean> call = myApi.getFirstComment(MyHeaders.getAppId(), MyHeaders.getAppSecret(), 1, shareId, 30);
    call.enqueue(new Callback<CommentBean>() {
        @Override
        public void onResponse(@NonNull Call<CommentBean> call, @NonNull Response<CommentBean> response) {
            if (response.isSuccessful()) {
                CommentBean firstCommentBean = response.body();
                if (firstCommentBean != null && firstCommentBean.getData() != null) {
                    // 这里我们需要异步加载每个评论的回复评论
                    callback.onSuccess(firstCommentBean);



                } else {
                    // 没有数据或数据格式错误
                    callback.onFailure(new Exception("No data or data format error"));
                }
            } else {
                // 请求失败
                callback.onFailure(new Exception("HTTP error: " + response.code()));
            }
        }

        @Override
        public void onFailure(@NonNull Call<CommentBean> call, @NonNull Throwable t) {
            // 网络请求本身失败
            callback.onFailure(t);
        }
    });
}

//定义获取二级评论的回调方法
public interface SecondCommentCallback {
    void onSuccess(CommentBean commentBean);
    void onFailure(Throwable throwable);
}

//    获取二级评论
public void loadSecondCommentData(long shareId, long firstCommentId, SecondCommentCallback callback) {
    ApiInterface myApi = MyRequest.request(); // 假设request()是你的Retrofit API接口的实例创建方法
    Call<CommentBean> call = myApi.getSecondComment(MyHeaders.getAppId(), MyHeaders.getAppSecret(), firstCommentId, 1, shareId, 30);
    call.enqueue(new Callback<CommentBean>() {
        @Override
        public void onResponse(@NonNull Call<CommentBean> call, @NonNull Response<CommentBean> response) {
            if (response.isSuccessful()) {
                CommentBean commentBean = response.body();
                if (commentBean != null) {
                    callback.onSuccess(commentBean);
                } else {
                    // 可以在这里处理body为null的情况，例如通过callback返回错误信息
                    callback.onFailure(new Throwable("Received null response body"));
                }
            } else {
                // 可以在这里添加更详细的错误处理
                callback.onFailure(new Throwable("HTTP error: " + response.code()));
            }
        }

        @Override
        public void onFailure(@NonNull Call<CommentBean> call, @NonNull Throwable t) {
            callback.onFailure(t);
        }
    });
}
}