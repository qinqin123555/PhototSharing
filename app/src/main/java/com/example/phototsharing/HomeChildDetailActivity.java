package com.example.phototsharing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.phototsharing.adapter.HomeChildDetailImageAdapter;
import com.example.phototsharing.entity.ShareBean;
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
    private ApiInterface myApiInterface;
    private Retrofit myRetrofit;
    private HomeChildDetailImageAdapter homeChildDetailImageAdapter;
    private ViewPager2 imageViewPager;



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

        imageViewPager = findViewById(R.id.home_child_detail_image_viewpager);
        myShareDetailBean = new ShareDetailBean();
        loadData(shareId);




    }


    private void loadData(long shareId) {
        myRetrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApiInterface = myRetrofit.create(ApiInterface.class);
        Call<ShareDetailBean> call = myApiInterface.getFindDetailInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),shareId,0);
        call.enqueue(new Callback<ShareDetailBean>() {
            @Override
            public void onResponse(@NonNull Call<ShareDetailBean> call, @NonNull Response<ShareDetailBean> response) {
                if (response.isSuccessful()) {
                    myShareDetailBean = response.body();
                    if (myShareDetailBean != null && myShareDetailBean.getData() != null){
                        homeChildDetailImageAdapter = new HomeChildDetailImageAdapter(getApplicationContext(),myShareDetailBean.getData().getImageUrlList());
                        imageViewPager.setAdapter(homeChildDetailImageAdapter);

                        imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                super.onPageScrollStateChanged(state);
                            }

                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                            }
                        });
                    }
                } else{
                    Log.e("TAG","接收数据失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareDetailBean> call, @NonNull Throwable t) {
                Log.e("TAG","网络请求失败");

            }
        });


    }
}