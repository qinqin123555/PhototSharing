package com.example.phototsharing.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.MyFragmentAdapter;
import com.example.phototsharing.fragment.HomeFragment;
import com.example.phototsharing.fragment.PersonalCenterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager2 myViewPager;
    private BottomNavigationView bottomNavigationView;
    private MyFragmentAdapter myFragmentAdapter;
    private List<Fragment> myFragmentList;
    private long myUserId;
    private String myUserName;
    private int follow;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            myUserId = receivedBundle.getLong("myUserId");
            myUserName = receivedBundle.getString("myUserName");
            follow = receivedBundle.getInt("total");
        }

        myViewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.btm_nav);

        //初始化主界面
        initData();
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),getLifecycle(),myFragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
        myViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        //使得Viewpager找到对应的fragment
        myViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        //底部导航栏监听事件
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    myViewPager.setCurrentItem(0);
                } else if (itemId == R.id.navigation_publish) {
                    myViewPager.setCurrentItem(1);
                } else if (itemId == R.id.navigation_mine) {
                    myViewPager.setCurrentItem(2);
                } else {
                    myViewPager.setCurrentItem(0);
                }

                return true;
            }
        });

    }

    private void initData() {
        myFragmentList = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance("首页",myUserName,myUserId);
        myFragmentList.add(homeFragment);
        //AddShareFragment fineFragment = AddShareFragment.newInstance("发现",myUserName,myUserId);
        PersonalCenterFragment mineFragment = PersonalCenterFragment.newInstance("我的",myUserName,myUserId,follow);
        //myFragmentList.add(fineFragment);
        myFragmentList.add(mineFragment);

    }
}