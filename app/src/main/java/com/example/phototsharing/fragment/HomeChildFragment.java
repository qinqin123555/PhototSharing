package com.example.phototsharing.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phototsharing.HomeChildDetailActivity;
import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeChildRecyclerViewAdapter;
import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.MyHeaders;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeChildFragment extends Fragment {

    View rootView;
    private RecyclerView myRecyclerView;
    private ShareBean myData;
    private HomeChildRecyclerViewAdapter homeChildRecyclerViewAdapter;
    private Context myContext;
    private ApiInterface myApiService;
    private  Retrofit retrofit;





    // TODO: Rename and change types and number of parameters
    public static HomeChildFragment newInstance(String param1) {
        HomeChildFragment fragment = new HomeChildFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = getActivity();





    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home_child, container, false);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化RecyclerView
        myRecyclerView = rootView.findViewById(R.id.home_child_recyclerview);
        // 设置LayoutManager（这里以LinearLayoutManager为例）
        myRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        System.out.println("1");

        loadData();
        homeChildRecyclerViewAdapter = new HomeChildRecyclerViewAdapter(myData, myContext);
        myRecyclerView.setAdapter(homeChildRecyclerViewAdapter);

        homeChildRecyclerViewAdapter.setOnItemClickListener(new HomeChildRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HomeChildDetailActivity.class);
                intent.putExtra("shareId",myData.getData().getRecords().get(position).getid());
                startActivity(intent);
            }
        });

        // 如果有需要，还可以设置SwipeRefreshLayout的刷新监听器
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.home_child_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 在这里执行刷新的逻辑，比如重新加载数据
            // ...

            // 刷新完成后停止刷新动画
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void loadData() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/") // 确保这里是有效的URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApiService = retrofit.create(ApiInterface.class);

        Call<ShareBean> call = myApiService.getFindInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),0);
        call.enqueue(new Callback<ShareBean>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ShareBean> call, @NonNull Response<ShareBean> response) {
                if (response.isSuccessful()) {
                    System.out.println("2");

                    myData = response.body();
                    if (myData != null) {
                        System.out.println("3");

                        // 更新适配器数据
                        homeChildRecyclerViewAdapter.changeAdapter(myData,myContext);
                        // 如果适配器有刷新UI的方法，也可以调用它
                        homeChildRecyclerViewAdapter.notifyDataSetChanged();
                        // 可以在这里添加额外的逻辑，如更新UI等
                    } else {
                        System.out.println("4");

                        Log.e("Tag", "数据为空");
                    }
                } else {
                    System.out.println("没接收到数据");

                    Log.d("TAG", "Response Code: " + response.code());
                    Log.d("TAG", "Response Body: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareBean> call, @NonNull Throwable t) {
                Log.e("Tag", "网络请求失败", t);
            }
        });
    }


}