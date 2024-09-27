package com.example.phototsharing.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAdapter extends FragmentStateAdapter {

    private List<Fragment> homeFragmentList = new ArrayList<>();
    private List<String> homeTitleList = new ArrayList<>();

    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragmentList,List<String> homeTitleList) {
        super(fragmentManager, lifecycle);
        homeFragmentList = fragmentList;
        this.homeTitleList = homeTitleList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return homeFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return homeFragmentList.size();
    }



    public CharSequence getPageTitle(int position){
        return homeTitleList.get(position);
    }

}
