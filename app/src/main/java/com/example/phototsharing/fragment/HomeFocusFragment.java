package com.example.phototsharing.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.HomeChildDetailImageAdapter;
import com.example.phototsharing.adapter.HomeFocusFragmentRecyclerViewAdapter;
import com.example.phototsharing.entity.HasFocusBean;
import com.example.phototsharing.net.GetFocusBeanCallback;
import com.example.phototsharing.net.MyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFocusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFocusFragment extends Fragment {
    private Context myContext;
    private long myUserId;
    private String myUserName;
    private HasFocusBean myHasFocusBean;

    private RecyclerView myRecyclerView;
    private HomeFocusFragmentRecyclerViewAdapter homeFocusFragmentRecyclerViewAdapter;

    private SwipeRefreshLayout swipeRefreshLayout ;





    public HomeFocusFragment() {
        // Required empty public constructor

    }


    public static HomeFocusFragment newInstance(long myUserId,String myUserName) {
        HomeFocusFragment fragment = new HomeFocusFragment();
        Bundle args = new Bundle();
        args.putLong("myUserId",myUserId);
        args.putString("myUserName",myUserName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
           myUserName = getArguments().getString("myUserName");
           myUserId = getArguments().getLong("myUserId");
        }
        myContext = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_focus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.home_focus_swiperefreshlayout);

        myRecyclerView = view.findViewById(R.id.home_focus_recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(myContext));
        homeFocusFragmentRecyclerViewAdapter = new HomeFocusFragmentRecyclerViewAdapter(myContext);
        myRecyclerView.setAdapter(homeFocusFragmentRecyclerViewAdapter);

        loadData();
        swipeRefreshLayout.setOnRefreshListener(() ->{
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        });


    }

    private void loadData() {
        MyRequest.getFocusInfo(0, 30, myUserId, new GetFocusBeanCallback() {
            @Override
            public void onSuccess(HasFocusBean hasFocusBean) {
                myHasFocusBean = hasFocusBean;
                homeFocusFragmentRecyclerViewAdapter.changeAdapter(myContext,hasFocusBean,myUserId);
                myRecyclerView.setAdapter(homeFocusFragmentRecyclerViewAdapter);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
            }
        });

    }


}