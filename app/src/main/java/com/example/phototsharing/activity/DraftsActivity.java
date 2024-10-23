package com.example.phototsharing.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.DraftAdapter;
import com.example.phototsharing.entity.ImageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DraftsActivity extends AppCompatActivity {

    private ListView draftsListView; // 声明 ListView
    private DraftAdapter draftAdapter;
    private List<ImageBean> drafts;

    private TextView draftstitle;
    private TextView draftscontent;
    private Button submitButton;
    private Button saveDraftButton;

    List<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts); // 绑定布局文件

        Uri localUri = Uri.parse("android.resource://com.example.phototsharing/" + R.drawable.add_pic_logo);
        imageUris.add(localUri);

        draftsListView = findViewById(R.id.item_draft); // 引用 ListView

        drafts = loadDrafts(); // 从 SharedPreferences 加载草稿

        // 设置适配器
        draftAdapter = new DraftAdapter(this, drafts);
        draftsListView.setAdapter(draftAdapter);
    }

    // 从 SharedPreferences 加载草稿列表
    private List<ImageBean> loadDrafts() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", Context.MODE_PRIVATE);
        String draftsJson = sharedPreferences.getString("draft_image_bean", "[]"); // 默认值为空列表

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ImageBean>>() {}.getType();
        return gson.fromJson(draftsJson, listType);
    }
}
