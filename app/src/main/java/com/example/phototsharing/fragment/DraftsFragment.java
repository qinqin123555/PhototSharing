package com.example.phototsharing.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.DraftAdapter;
import com.example.phototsharing.entity.ImageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DraftsFragment extends Fragment {

    private ListView draftsListView; // 声明 ListView
    private DraftAdapter draftAdapter;
    private List<ImageBean> drafts; // 使用 ImageBean 列表

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drafts, container, false);

        draftsListView = view.findViewById(R.id.drafts_list_view); // 确保引用正确的 ID
        drafts = loadDrafts(); // 从 SharedPreferences 加载草稿

        draftAdapter = new DraftAdapter(getContext(), drafts);
        draftsListView.setAdapter(draftAdapter);

        return view;
    }

    private List<ImageBean> loadDrafts() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_data", Context.MODE_PRIVATE);
        String draftsJson = sharedPreferences.getString("draft_image_bean", "[]"); // 默认值为空列表

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ImageBean>>() {}.getType();
        return gson.fromJson(draftsJson, listType);
    }
}
