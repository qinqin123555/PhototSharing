package com.example.phototsharing.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentStateAdapter {

    List<Fragment> myFragmentList = new ArrayList<>();

    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<Fragment> mFragmentList) {
        super(fragmentManager, lifecycle);
        myFragmentList = mFragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return myFragmentList.size();
    }
}
