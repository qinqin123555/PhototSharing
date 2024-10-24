package com.example.phototsharing.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

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

    private ListView draftsListView;
    private DraftAdapter draftAdapter;
    private List<ImageBean> drafts;
    private List<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts); // 绑定布局文件

        Uri localUri = Uri.parse("android.resource://com.example.phototsharing/" + R.drawable.add_pic_logo);
        imageUris.add(localUri);

        draftsListView = findViewById(R.id.item_draft); // 引用 ListView

        // 写入初始数据到 SharedPreferences
        saveInitialDraftData();

        drafts = loadDrafts(); // 从 SharedPreferences 加载草稿

        // 设置适配器
        draftAdapter = new DraftAdapter(this, drafts);
        draftsListView.setAdapter(draftAdapter);
    }

    // 写入初始数据到 SharedPreferences
    private void saveInitialDraftData() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 检查草稿数据是否已经存在，避免重复写入
        if (!sharedPreferences.contains("draft_image_bean")) {
            List<ImageBean> initialDrafts = new ArrayList<>();

            // 第一个初始数据
            ImageBean firstDraft = new ImageBean();
            firstDraft.setImageCode(1001L);  // 设置图片代码
            firstDraft.setMsg("测试草稿箱标题"); // 设置消息/标题

            // 设置第一个 ImageBean 的 ImageUrlList
            List<String> firstImageUrlList = new ArrayList<>();
            firstImageUrlList.add(imageUris.get(0).toString()); // 使用初始化图片
            firstDraft.setImageUrlList(firstImageUrlList);

            // 设置 Data 对象为空，或者根据需要初始化
            ImageBean.Data firstData = new ImageBean.Data();
            firstData.setImageUrlList(firstImageUrlList);
            firstDraft.setData(firstData);

            // 第二个初始数据
            ImageBean secondDraft = new ImageBean();
            secondDraft.setImageCode(1002L); // 设置图片代码
            secondDraft.setMsg("今天是校运会"); // 设置消息/标题

            // 设置第二个 ImageBean 的 ImageUrlList
            List<String> secondImageUrlList = new ArrayList<>();
            secondImageUrlList.add("https://example.com/sport_day_image.jpg"); // 指定的外部图片
            secondDraft.setImageUrlList(secondImageUrlList);

            // 设置第二个 Data 对象
            ImageBean.Data secondData = new ImageBean.Data();
            secondData.setImageUrlList(secondImageUrlList);
            secondDraft.setData(secondData);

            // 添加两个草稿到列表
            initialDrafts.add(firstDraft);
            initialDrafts.add(secondDraft);

            // 使用 Gson 序列化数据
            Gson gson = new Gson();
            String draftsJson = gson.toJson(initialDrafts);
            editor.putString("draft_image_bean", draftsJson);
            editor.apply(); // 提交更改
        }
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
