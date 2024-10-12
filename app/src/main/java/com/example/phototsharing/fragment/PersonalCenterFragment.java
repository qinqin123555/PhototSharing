package com.example.phototsharing.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeFocusFragmentRecyclerViewAdapter;
import com.example.phototsharing.adapter.HomeFragmentAdapter;
import com.example.phototsharing.entity.HasFocusBean;
import com.example.phototsharing.net.GetFocusBeanCallback;
import com.example.phototsharing.net.MyRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalCenterFragment extends Fragment {

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
    private TextView profileUserId, subscriptionNumber, fanNumber, thumbsupNumber;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    public static PersonalCenterFragment newInstance(String title, String myUserName,long myUserId,int follow) {
        PersonalCenterFragment fragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, myUserName);
        args.putLong(ARG_PARAM3,myUserId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_center, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewPager = view.findViewById(R.id.home_page);
        homeTabLayout = view.findViewById(R.id.home_tab_layout);

        initData();
        homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager(),getLifecycle(),homeFragmentList,homeItemTitles);
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

        // 加载用户数据
        loadUserData();

    }

    private void initData() {
        homeFragmentList = new ArrayList<>();
        homeItemTitles = new ArrayList<>();
        HomeLikeFragment homeLikeFragment = HomeLikeFragment.newInstance(myUserId,myUserName);
        HomeFocusFragment homeFocusFragment = HomeFocusFragment.newInstance(myUserId,myUserName);
        homeFragmentList.add(homeFocusFragment);
        homeFragmentList.add(homeLikeFragment);
        homeItemTitles.add("我的");
        homeItemTitles.add("喜欢");
    }

    //加载用户数据的方法
    private void loadUserData() {

        MyRequest.getFocusInfo(0, 30, myUserId, new GetFocusBeanCallback() {
            @Override
            public void onSuccess(HasFocusBean hasFocusBean) {

                // 这里假设 `hasFocusBean` 中包含了关注数、粉丝数和获赞数
                int subscriptions = (int) hasFocusBean.getData().getTotal();  // 关注数
//                int fans = hasFocusBean.getFanCount();                    // 粉丝数
//                int thumbsUp = hasFocusBean.getThumbsUpCount();           // 获赞数

                // 更新视图控件的内容
                subscriptionNumber.setText(String.valueOf(subscriptions));
//                fanNumber.setText(String.valueOf(fans));
//                thumbsupNumber.setText(String.valueOf(thumbsUp));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
            }
        });
    }



}