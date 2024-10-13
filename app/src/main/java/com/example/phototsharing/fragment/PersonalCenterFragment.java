package com.example.phototsharing.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeFocusFragmentRecyclerViewAdapter;
import com.example.phototsharing.adapter.HomeFragmentAdapter;
import com.example.phototsharing.entity.HasFocusBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.GetFocusBeanCallback;
import com.example.phototsharing.net.GetUserInfoCallback;
import com.example.phototsharing.net.MyRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import android.view.MenuItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalCenterFragment extends Fragment {

    View rootView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "myUserName";
    private static final String ARG_PARAM3 = "myUserId";

    private ViewPager2 homeViewPager;
    private TabLayout homeTabLayout;
    private List<Fragment> homeFragmentList;
    private List<String> homeItemTitles;
    private HomeFragmentAdapter homeFragmentAdapter;

    private long myUserId;
    private String myUserName;
    private HasFocusBean myHasFocusBean;
    private HomeFocusFragmentRecyclerViewAdapter homeFocusFragmentRecyclerViewAdapter;
    private RecyclerView myRecyclerView;
    private Context myContext;

    // 新增：声明控件变量
    private CircleImageView avatar;
    private TextView profileUserId, subscriptionNumber, fanNumber, thumbsupNumber, personal_description;
    private ImageView sexImageView;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    public static PersonalCenterFragment newInstance(String title, String myUserName, long myUserId, int follow) {
        PersonalCenterFragment fragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, myUserName);
        args.putLong(ARG_PARAM3, myUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            myUserId = getArguments().getLong("myUserId");
            myUserName = getArguments().getString("myUserName");
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context; // 初始化 myContext
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_personal_center, container, false);
        }


        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        homeViewPager = view.findViewById(R.id.home_page);
        homeTabLayout = view.findViewById(R.id.home_tab_layout);

        initData();
        homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager(), getLifecycle(), homeFragmentList, homeItemTitles);
        homeViewPager.setAdapter(homeFragmentAdapter);
//        homeViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(homeTabLayout, homeViewPager,
                (tab, position) -> tab.setText(homeItemTitles.get(position)));
        tabLayoutMediator.attach();

        // 新增：绑定视图控件
        avatar = view.findViewById(R.id.avatar);
        profileUserId = view.findViewById(R.id.profile_user_id);
        subscriptionNumber = view.findViewById(R.id.subscription_number);
        fanNumber = view.findViewById(R.id.fan_number);
        thumbsupNumber = view.findViewById(R.id.thumbsup_number);
        personal_description = view.findViewById(R.id.personal_description);
        sexImageView = view.findViewById(R.id.sex);

        // 加载用户数据
        loadUserData();

        // 如果有需要，还可以设置SwipeRefreshLayout的刷新监听器
//        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(() -> {
//            // 更新适配器数据
//            loadUserData();
//            swipeRefreshLayout.setRefreshing(false);
//        });

        // 设置下拉刷新监听器
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadUserData();;
//            }
//        });

    }

    private void initData() {
        homeFragmentList = new ArrayList<>();
        homeItemTitles = new ArrayList<>();
        HomeLikeFragment homeLikeFragment = HomeLikeFragment.newInstance(myUserId, myUserName);
        HomeFocusFragment homeFocusFragment = HomeFocusFragment.newInstance(myUserId, myUserName);
        homeFragmentList.add(homeFocusFragment);
        homeFragmentList.add(homeLikeFragment);
        homeItemTitles.add("我的");
        homeItemTitles.add("喜欢");
        // 滚动到顶部
//        if (homeViewPager.getChildAt(0) instanceof RecyclerView) {
//            RecyclerView recyclerView = (RecyclerView) homeViewPager.getChildAt(0);
//            if (recyclerView.getLayoutManager() != null) {
//                recyclerView.getLayoutManager().scrollToPosition(0);
//            }
//        }
    }

    //加载用户数据的方法
    private void loadUserData() {

        MyRequest.getFocusInfo(0, 30, myUserId, new GetFocusBeanCallback() {
            @Override
            public void onSuccess(HasFocusBean hasFocusBean) {

                myHasFocusBean = hasFocusBean;

                // 获取 records 列表
                List<HasFocusBean.Data.Record> records = hasFocusBean.getData().getRecords();

                // 统计 hasFocus 为 true 的数量
                int focusCount = 0;
                for (HasFocusBean.Data.Record record : records) {
                    if (record.getHasFocus()) {
                        focusCount++;
                    }
                }

                // 将关注人数绑定到 TextView 上
                subscriptionNumber.setText(String.valueOf(focusCount));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
            }
        });

        MyRequest.getUserByName(myUserName, new GetUserInfoCallback() {
            @Override
            public void onSuccess(PersonBean personBean) {
                // 获取头像的 URL
                String avatarUrl = personBean.getData().getAvatar();
                String bio = personBean.getData().getIntroduce();  // 获取简介
                int gender = personBean.getData().getSex();  // 获取性别

                // 使用 Glide 加载头像到 CircleImageView (avatar)
                if (isAdded()) {
                    Glide.with(requireContext())
                            .load(avatarUrl)  // 加载 URL
                            .placeholder(R.drawable.avatar)  // 占位图，可以是默认头像
                            .error(R.drawable.avatar)  // 错误时显示的图片
                            .into(avatar);  // 设置到 CircleImageView 组件上
                }

                personal_description.setText(bio);
                profileUserId.setText(myUserName);

                // 判断性别并设置相应的图片
                if (gender == 0) {
                    // 如果性别是 0（女性），设置为女性图片
                    sexImageView.setImageResource(R.drawable.woman);
                } else if (gender == 1) {
                    // 如果性别是 1（男性），设置为男性图片
                    sexImageView.setImageResource(R.drawable.man);
                }


            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(myContext, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                Log.e("PersonalCenterFragment", "无头像", throwable);
            }
        });
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.home_topbar_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId(); // 获取传入的菜单项 ID
//        Log.d("PersonalCenterFragment", "Selected item ID: " + itemId); // 打印菜单项 ID
//
//        switch (item.getItemId()) {
//            case R.id.change_info:
//                // 处理更改信息的逻辑
//                Toast.makeText(myContext, "更改信息", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.back_login:
//                // 处理登出的逻辑
//                Toast.makeText(myContext, "登出", Toast.LENGTH_SHORT).show();
//                // 可以执行跳转到登录页面的逻辑
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//        return false;
//    }

}