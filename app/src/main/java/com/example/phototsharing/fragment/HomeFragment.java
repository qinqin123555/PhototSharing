package com.example.phototsharing.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phototsharing.adapter.HomeFragmentAdapter;
import com.example.phototsharing.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private ViewPager2 homeViewPager;
    private TabLayout homeTabLayout;
    private List<Fragment> homeFragmentList;
    private List<String> homeItemTitles;
    private HomeFragmentAdapter homeFragmentAdapter;



    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewPager = view.findViewById(R.id.home_viewpager);
        homeTabLayout = view.findViewById(R.id.home_tab_layout);

        initData();
        homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager(),getLifecycle(),homeFragmentList,homeItemTitles);
        homeViewPager.setAdapter(homeFragmentAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(homeTabLayout, homeViewPager,
                (tab, position) -> tab.setText(homeItemTitles.get(position)));
        tabLayoutMediator.attach();
    }

    private void initData() {
        homeFragmentList = new ArrayList<>();
        homeItemTitles = new ArrayList<>();
        HomeChildFragment homeChildFragment1 = HomeChildFragment.newInstance("关注");
        HomeChildFragment homeChildFragment2 = HomeChildFragment.newInstance("发现");
        homeFragmentList.add(homeChildFragment1);
        homeFragmentList.add(homeChildFragment2);
        homeItemTitles.add("关注");
        homeItemTitles.add("发现");

    }
}