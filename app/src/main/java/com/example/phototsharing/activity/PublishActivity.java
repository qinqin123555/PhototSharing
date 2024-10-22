package com.example.phototsharing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.phototsharing.R;
import com.example.phototsharing.databinding.ActivityPublishBinding;
import com.example.phototsharing.entity.ImageBean;
import com.example.phototsharing.fragment.PublishFragment;
import com.example.phototsharing.fragment.UploadFragment;
import com.example.phototsharing.net.ImageUploader;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.URLS;
import com.example.phototsharing.net.UploadCallback;
import com.example.phototsharing.utilis.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements UploadFragment.OnUploadFragmentInteractionListener {

    private ActivityPublishBinding binding;
    private String imageCode;  // 用来保存选中的图片路径
    private EditText titleEditText, contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // 加载 PublishFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.publish_fragment_container, new PublishFragment());
        transaction.replace(R.id.upload_fragment_container, new UploadFragment());
        transaction.commit();
    }

    // 加载上传图片的 Fragment
    private void loadUploadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        UploadFragment uploadFragment = new UploadFragment();
        fragmentManager.beginTransaction()
                .add(R.id.publish_fragment_container, uploadFragment)
                .commit();
    }

    // 发布逻辑
    private void publish() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        // 确保标题、正文和图片都不为空
        if (!title.isEmpty() && !content.isEmpty() && imageCode != null) {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("title", title);
            bodyMap.put("content", content);
            bodyMap.put("imageCode", imageCode);
            bodyMap.put("pUserId", SharedPreferencesUtil.getValue(this, "id"));

            // 将数据转换为 JSON 格式
            Gson gson = new Gson();
            String json = gson.toJson(bodyMap);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            // 发送发布请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URLS.ADD_SHARE.getUrl())
                    .headers(MyHeaders.getHeaders())
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();  // 关闭当前 Activity
                        });
                    }
                }
            });

        } else {
            Toast.makeText(this, "标题、内容和图片不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    // 保存草稿逻辑
    private void saveDraft() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("title", title);
        bodyMap.put("content", content);
        bodyMap.put("imageCode", imageCode);
        bodyMap.put("pUserId", SharedPreferencesUtil.getValue(this, "id"));

        Gson gson = new Gson();
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
                runOnUiThread(() -> Toast.makeText(PublishActivity.this, "草稿保存失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(PublishActivity.this, "草稿保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        });
    }

    // Fragment 回调，获取选中的图片路径，并上传图片
    @Override
    public void onImageSelected(String imagePath) {
        File imageFile = new File(imagePath);
        String fileType = "image/jpeg";  // 假设上传的图片类型为 JPEG

        // 上传图片
        ImageUploader imageUploader = new ImageUploader();
        imageUploader.uploadImage(imageFile, fileType, new UploadCallback() {
            @Override
            public void onSuccess(ImageBean imageBean) {
                // 获取上传成功的 imageCode
                long imageCode = imageBean.getData().getImageCode();  // imageCode 是 long 类型

                // 如果你需要把 long 转换为 String：
                String imageCodeStr = String.valueOf(imageCode);

                runOnUiThread(() -> {
                    Toast.makeText(PublishActivity.this, "图片上传成功, 图片代码: " + imageCodeStr, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(() -> {
                    Toast.makeText(PublishActivity.this, "图片上传失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


}
