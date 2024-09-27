package com.example.phototsharing.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.MyFragmentAdapter;
import com.example.phototsharing.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 myViewPager;
    private BottomNavigationView bottomNavigationView;
    private MyFragmentAdapter myFragmentAdapter;
    private List<Fragment> myFragmentList;

    @SuppressLint("MissingInflatedId")
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


        myViewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.btm_nav);

        //初始化主界面
        initeData();
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),getLifecycle(),myFragmentList);
        myViewPager.setAdapter(myFragmentAdapter);

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



    }

    private void initeData() {
        myFragmentList = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance("首页","");
        myFragmentList.add(homeFragment);
        HomeFragment fineFragment = HomeFragment.newInstance("发现","");
        HomeFragment mineFragment = HomeFragment.newInstance("我的","");
        myFragmentList.add(fineFragment);
        myFragmentList.add(mineFragment);


    }
}