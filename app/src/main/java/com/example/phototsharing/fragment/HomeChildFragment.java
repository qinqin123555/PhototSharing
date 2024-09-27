package com.example.phototsharing.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeChildFragment extends Fragment {

    View rootView;
    private MutableLiveData<ShareBean> myData;
    private HomeChildRecyclerViewAdapter homeChildRecyclerViewAdapter;
    private Context myContext;
    private RecyclerView myRecyclerView;


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
        myData = new MutableLiveData<>();

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
        homeChildRecyclerViewAdapter = new HomeChildRecyclerViewAdapter(myContext);
        System.out.println("1");

        loadData();

        homeChildRecyclerViewAdapter.setOnItemClickListener(new HomeChildRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HomeChildDetailActivity.class);
                intent.putExtra("shareId", Objects.requireNonNull(myData.getValue()).getData().getRecords().get(position).getid());
                startActivity(intent);
            }
        });

        // 如果有需要，还可以设置SwipeRefreshLayout的刷新监听器
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.home_child_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 更新适配器数据
            loadData();

            swipeRefreshLayout.setRefreshing(false);
        });
    }


    private void loadData() {
        // 确保这里是有效的URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/") // 确保这里是有效的URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface myApiService = retrofit.create(ApiInterface.class);
        Call<ShareBean> call = myApiService.getFindInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),0);
        call.enqueue(new Callback<ShareBean>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ShareBean> call, @NonNull Response<ShareBean> response) {
                if (response.isSuccessful()) {
                    System.out.println("2");

                    myData.setValue(response.body());
                    if (myData != null) {
                        System.out.println("3");
                        homeChildRecyclerViewAdapter.changeAdapter(myData.getValue(),myContext);
                        myRecyclerView.setAdapter(homeChildRecyclerViewAdapter);

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