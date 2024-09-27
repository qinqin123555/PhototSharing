package com.example.phototsharing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.phototsharing.adapter.HomeChildDetailImageAdapter;
import com.example.phototsharing.entity.ShareDetailBean;
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.MyHeaders;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeChildDetailActivity extends AppCompatActivity {
    private ShareDetailBean myShareDetailBean;
    private HomeChildDetailImageAdapter homeChildDetailImageAdapter;
    private Context myContext;

    private ImageView backImg;
    private ImageView profilePicture;
    private TextView userName;
    private Button focusBtn;

    private ScrollView homeChildDetailScrollView;
    private RecyclerView homeChildDetailImageRecyclerView;
    private TextView homeChildDetailTitle;
    private TextView homeChildDetailContent;
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
        Intent intent = getIntent();
        long shareId = intent.getLongExtra("shareId",0);

        //初始化一些参数
        myContext = getApplicationContext();
        myShareDetailBean = new ShareDetailBean();
        homeChildDetailImageAdapter = new HomeChildDetailImageAdapter();
        homeChildDetailScrollView = findViewById(R.id.home_child_detail_scroll_view);
        homeChildDetailImageRecyclerView = findViewById(R.id.home_child_detail_image_recycler_view);



        //网络请求数据，并渲染UI
        loadData(shareId);

    }


    private void loadData(long shareId) {
        Retrofit myRetrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface myApiInterface = myRetrofit.create(ApiInterface.class);
        Call<ShareDetailBean> call = myApiInterface.getFindDetailInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),shareId, 1838948060948992000L);
        call.enqueue(new Callback<ShareDetailBean>() {
            @Override
            public void onResponse(@NonNull Call<ShareDetailBean> call, @NonNull Response<ShareDetailBean> response) {
                if (response.isSuccessful()) {
                    myShareDetailBean = response.body();

                    if (myShareDetailBean != null){
                        Log.e("TAG", String.valueOf(response.body()));

                        Log.e("TAG", String.valueOf(shareId));

                        initView(myShareDetailBean);

                    }
                } else{

                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareDetailBean> call, @NonNull Throwable t) {
                Log.e("TAG","网络请求失败");

            }
        });
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initView(ShareDetailBean shareDetailBean){
        backImg = findViewById(R.id.iv_back);
        profilePicture = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.username);
        focusBtn = findViewById(R.id.focus);
        homeChildDetailTitle = findViewById(R.id.tv_home_child_detail_title);
        homeChildDetailContent = findViewById(R.id.tv_home_child_detail_content);
        homeChildDetailCommentEditText = findViewById(R.id.home_child_detail_comment_edit_text);
        icLike = findViewById(R.id.iv_home_child_detail_like);
        likeNum = findViewById(R.id.tv_home_child_detail_like_number);
        icCollect = findViewById(R.id.iv_home_child_detail_collect);
        collectNum = findViewById(R.id.tv_home_child_detail_collect_number);


        homeChildDetailImageAdapter.changeAdapter(myContext,shareDetailBean.getData().getImageUrlList());
        homeChildDetailImageRecyclerView.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false));
        homeChildDetailImageRecyclerView.setAdapter(homeChildDetailImageAdapter);

        if (shareDetailBean.getData().getUsername() != null) {
            userName.setText(shareDetailBean.getData().getUsername());
        } else {
            userName.setText("username");
        }

        if (shareDetailBean.getData().getTitle() != null) {
            homeChildDetailTitle.setText(shareDetailBean.getData().getTitle());
        } else {
            homeChildDetailTitle.setText("标题");
        }
        if (shareDetailBean.getData().getContent() != null) {
            homeChildDetailContent.setText(shareDetailBean.getData().getContent());
        } else {
            homeChildDetailContent.setText("内容");
        }
        if (shareDetailBean.getData().getLikeNum() != null) {
            likeNum.setText(String.format("%d",shareDetailBean.getData().getLikeNum()));
        } else {
            likeNum.setText("0");
        }

        if (shareDetailBean.getData().getCollectNum() != null) {
            collectNum.setText(String.format("%d",shareDetailBean.getData().getCollectNum()));
        } else {
            collectNum.setText("0");
        }


    }
}