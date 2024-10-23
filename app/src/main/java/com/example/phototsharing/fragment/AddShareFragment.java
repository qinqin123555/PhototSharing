package com.example.phototsharing.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

//import com.example.phototsharing.MyInfo.EditMyInfo;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.AddLikeBean;
import com.example.phototsharing.entity.ImageBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.PublishRepository;
import com.example.phototsharing.net.URLS;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShareFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USERID = "userId";

    private String title;
    private String username;
    private long userId;
    List<File> imgFile = new ArrayList<>();

    private EditText textContentEditText;
    private Button submitButton;
    private Button saveDraftButton;
    private ImageView uploadImageView;
    private String imageCode;  // 保存选中的图片路径
    private PublishRepository publishRepository;

    public static AddShareFragment newInstance(String title, String myUserName, long myUserId) {
        AddShareFragment fragment = new AddShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_USERNAME, myUserName);
        args.putLong(ARG_USERID, myUserId);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageBean imageBean; // 新增 ImageBean 实例

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            username = getArguments().getString(ARG_USERNAME);
            userId = getArguments().getLong(ARG_USERID);
        }
        publishRepository = new PublishRepository(getContext());
        imageBean = new ImageBean(); // 初始化 ImageBean
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_publish, container, false);

        textContentEditText = view.findViewById(R.id.text);
        submitButton = view.findViewById(R.id.submit_button);
        saveDraftButton = view.findViewById(R.id.draft_button);
        uploadImageView = view.findViewById(R.id.custom_image); // 上传图片的 ImageView

        // 上传图片按钮点击，打开系统相册
        uploadImageView.setOnClickListener(v -> openImagePicker());
        String content = textContentEditText.getText().toString();

        //点击发布按钮
        submitButton.setOnClickListener(v -> publishContent(title, content, imageCode, userId, new PublishCallback() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        }));

        //点击保存草稿按钮
        saveDraftButton.setOnClickListener(v -> saveDraft(title, content, imageCode, userId, new PublishCallback() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        }));

        return view;
    }

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    //定义回调接口
    public interface UploadCallback {
        void onSuccess(ResponseBody imageBean);
        void onFailure(Throwable t);
    }

    private void uploadImage(File file, String description, UploadCallback callback) {
        ApiInterface myApi = MyRequest.request();

        // 创建描述的 RequestBody
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), file);

        // 创建文件的 RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

        // 使用 MultipartBody.Part 封装文件
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        filePart.
                // 调用接口上传图片
                Call<ResponseBody> call = myApi.uploadImage(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                filePart
        );

        // 执行异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("HTTP错误: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // 打开系统相册选择图片
    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            // 如果有权限，打开相册
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，打开相册
                openImagePicker();
            } else {
                // 权限被拒绝，给出提示
                Toast.makeText(getContext(), "需要读取存储权限以选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 处理选中图片后的逻辑
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (uploadImageView != null) {
                uploadImageView.setImageURI(imageUri);

                // 将 URI 转换为文件对象进行上传
                File file = new File(getRealPathFromURI(imageUri));
                handleUpload(file, "图片描述");

                List<MultipartBody.Part> fileParts = new ArrayList<>();
                for (File file : files) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", file.getName(), fileBody);
                    fileParts.add(filePart);
                }

                // 调用接口上传多个文件
                Call<ResponseBody> call = myApi.publishContent(
                        MyHeaders.getAppId(),
                        MyHeaders.getAppSecret(),
                        fileParts
                );
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        // 转换 URI 为文件路径
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(columnIndex);
        }
        return null;
    }

    private void handleUpload(File file, String description) {
        uploadImage(file, description, new UploadCallback() {
            @Override
            public void onSuccess(ResponseBody imageBean) {
                // 处理成功上传后的逻辑
                Toast.makeText(getContext(), "图片上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // 处理上传失败的逻辑
                Toast.makeText(getContext(), "上传失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface PublishCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }

    private OkHttpClient client = new OkHttpClient(); // 初始化 OkHttpClient
    private Gson gson = new Gson(); // 初始化 Gson

   /* private void sendRequest(String url, Map<String, Object> bodyMap, PublishCallback callback) {
        String json = gson.toJson(bodyMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)
                .headers(MyHeaders.getHeaders())
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("操作成功");
                } else {
                    callback.onError("操作失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onError("请求失败: " + e.getMessage());

            }
        });
    }*/

    // 发布内容
    private void publishContent(String title, String content, String imageCode, long userId, PublishCallback callback) {

        ApiInterface myApi = MyRequest.request();


        Call<AddLikeBean> call = myApi.publishContent(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                title,
                content,
                imageCode,
                userId
        );

        call.enqueue(new Callback<AddLikeBean>() {
            @Override
            public void onResponse(Call<AddLikeBean> call, Response<AddLikeBean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 检查返回的代码
                    if (response.body().getCode() == 200) {
                        callback.onSuccess(String.valueOf(response.body()));
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<AddLikeBean> call, Throwable t) {

            }
        });



       /* if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            callback.onError("标题或内容不能为空");
            return;
        }

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("title", title);
        bodyMap.put("content", content);
        bodyMap.put("imageCode", imageCode);
        bodyMap.put("pUserId", userId);

        sendRequest(URLS.ADD_SHARE.getUrl(), bodyMap, callback);*/
    }

    // 发布按钮点击事件
    private void onPublishButtonClick() {
        String title = textContentEditText.getText().toString(); // 获取标题
        String content = textContentEditText.getText().toString(); // 获取内容
        long userId = this.userId; // 用户ID
        String imageCode = this.imageCode; // 获取图片代码

        publishContent(title, content, imageCode, userId, new PublishCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                // 发布成功后可以导航到个人中心
                navigateToFragment(new PersonalCenterFragment(), R.id.fragment_personal_center);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "发布失败: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 保存草稿
    private void saveDraft(String title, String content, String imageCode, long userId, PublishCallback callback) {
       /* if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            callback.onError("标题或内容不能为空");
            return;
        }

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("title", title);
        bodyMap.put("content", content);
        bodyMap.put("imageCode", imageCode);
        bodyMap.put("pUserId", userId);

        sendRequest(URLS.SAVE.getUrl(), bodyMap, callback);*/
    }

    // 保存草稿按钮点击事件
    private void onSaveDraftButtonClick() {
        String title = textContentEditText.getText().toString(); // 获取标题
        String content = textContentEditText.getText().toString(); // 获取内容
        long userId = this.userId; // 用户ID
        String imageCode = this.imageCode; // 获取图片代码

        saveDraft(title, content, imageCode, userId, new PublishCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "草稿保存成功", Toast.LENGTH_SHORT).show();
                // 保存成功后可以导航到草稿页面
                navigateToFragment(new DraftsFragment(), R.id.activity_drafts);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "草稿保存失败: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void navigateToFragment(Fragment fragment, int containerId) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }




}


