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
import android.widget.Toast;

import com.example.phototsharing.activity.HomeFindDetailActivity;
import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeFindRecyclerViewAdapter;
import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.net.GetShareBeanCallback;
import com.example.phototsharing.net.MyRequest;


public class HomeFindFragment extends Fragment {

    View rootView;
    private ShareBean myShareBean;
    private HomeFindRecyclerViewAdapter homeFindRecyclerViewAdapter;
    private Context myContext;
    private RecyclerView myRecyclerView;
    private long myUserId;
    private String myUserName;




    // TODO: Rename and change types and number of parameters
    public static HomeFindFragment newInstance(String myUserName,long myUserId) {
        HomeFindFragment fragment = new HomeFindFragment();
        Bundle args = new Bundle();
        args.putString("myUserName",myUserName);
        args.putLong("myUserId",myUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = getActivity();

        if (getArguments() != null) {
            myUserId = getArguments().getLong("myUserId");
            Log.d("myUserId",String.valueOf(myUserId));
            myUserName = getArguments().getString("myUserName");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home_find, container, false);
        }
        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // 初始化RecyclerView
        myRecyclerView = rootView.findViewById(R.id.home_child_recyclerview);
        // 设置LayoutManager（这里以LinearLayoutManager为例）
        myRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        homeFindRecyclerViewAdapter = new HomeFindRecyclerViewAdapter(myContext,myUserId);
        myRecyclerView.setAdapter(homeFindRecyclerViewAdapter);

        loadData();

        homeFindRecyclerViewAdapter.setOnItemClickListener(new HomeFindRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                myShareBean = homeFindRecyclerViewAdapter.getShareBean();
                homeFindRecyclerViewAdapter.notifyItemChanged(position);

                long shareId = myShareBean.getData().getRecords().get(position).getid();
                String username = myShareBean.getData().getRecords().get(position).getUsername();
                String avatar = myShareBean.getData().getRecords().get(position).getAvatar();


                Bundle bundle = new Bundle();
                bundle.putLong("shareId", shareId);
                bundle.putString("username",username);
                bundle.putString("avatar",avatar);
                bundle.putString("myUserName",myUserName);
                bundle.putLong("myUserId",myUserId);
                Intent intent = new Intent(getActivity(), HomeFindDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        System.out.println("1");



        // 如果有需要，还可以设置SwipeRefreshLayout的刷新监听器
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.home_child_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 更新适配器数据
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }


    private void loadData() {
        Log.d("myUserId",String.valueOf(myUserId));

        MyRequest.getShareBeanData(myUserId, new GetShareBeanCallback() {
            @Override
            public void onSuccess(ShareBean shareBean) {
                myShareBean = shareBean;
                homeFindRecyclerViewAdapter.changeAdapter(shareBean,myContext);
                myRecyclerView.setAdapter(homeFindRecyclerViewAdapter);

            }
            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(myContext, "加载页面失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}