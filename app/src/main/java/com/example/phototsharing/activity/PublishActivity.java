package com.example.phototsharing.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.phototsharing.R;
//import com.example.phototsharing.databinding.ActivitySubmitBinding;
import com.example.phototsharing.entity.ImageBean;
import com.example.phototsharing.fragment.UploadFragment;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.URLS;
import com.example.phototsharing.utilis.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements UploadFragment.OnUploadFragmentInteractionListener {

   /* private ActivitySubmitBinding binding;
    private EditText sub_title;
    private EditText text;
    private String imageCode;

    // Handle处理toast显示消息
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    ImageBean imageBean = (ImageBean) msg.obj;
                    // 获取图片编码信息
                    imageCode = imageBean.getData().getImageCode();
                    break;
                case 2:
                    Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 3:
                    Toast.makeText(PublishActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  binding = ActivitySubmitBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_submit);
        View view = binding.getRoot();
        setContentView(view);

        // 隐藏ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        sub_title = binding.subTitle;
        text = binding.subTitle;

        // 设置工具栏的导航按钮点击事件
        binding.submitToolbar.setNavigationOnClickListener(view1 -> showExitDialog("提示", "是否保存草稿再退出？", "是", "否", true));

        // 加载 UploadFragment 到当前的 PublishActivity
        loadUploadFragment();

        // 发布按钮点击事件
        binding.submitButton.setOnClickListener(view12 -> publish());
*/
    }

    private void loadUploadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container); // 使用 fragment_container
        if (fragment == null) {
            fragment = new UploadFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment) // 使用 fragment_container
                    .commit();
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    // 弹窗显示内容
    private void showExitDialog(String title, String message, String first, String second, boolean flag) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(second, (dialogInterface, i) -> {
                    if (flag) finish();
                })
                .setNegativeButton(first, (dialogInterface, i) -> {
                    if (flag) save();
                })
                .show();
    }

    // 发布逻辑
    private void publish() {
     /*   Gson gson = new Gson();
        String title = binding.subTitle.getText().toString();

        if (!title.equals("") && !text.equals("") && imageCode != null) {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("content", text);
            bodyMap.put("imageCode", imageCode);
            bodyMap.put("pUserId", SharedPreferencesUtil.getValue(PublishActivity.this, "id"));
            bodyMap.put("title", title);

            String json = gson.toJson(bodyMap);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URLS.ADD_SHARE.getUrl())
                    .headers(MyHeaders.getHeaders())
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }
            });

        } else {
            Toast.makeText(PublishActivity.this, "标题,内容和图片不能为空", Toast.LENGTH_SHORT).show();
        }*/
    }

    // 保存逻辑
    private void save() {
   /*     Gson gson = new Gson();
        String title = binding.subTitle.getText().toString();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("content", text);
        bodyMap.put("imageCode", imageCode);
        bodyMap.put("pUserId", SharedPreferencesUtil.getValue(PublishActivity.this, "id"));
        bodyMap.put("title", title);

        String json = gson.toJson(bodyMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URLS.SAVE.getUrl())
                .headers(MyHeaders.getHeaders())
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        });*/
    }

    @Override
    public void onImageSelected(String imagePath) {
/*
        imageCode = imagePath;  // 可以通过Fragment来回传数据，处理图片选择后的逻辑
*/
    }
}
