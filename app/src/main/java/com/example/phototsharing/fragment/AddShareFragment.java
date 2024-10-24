package com.example.phototsharing.fragment;

//import manifests.AndroidManifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.example.phototsharing.activity.DraftsActivity;
import com.example.phototsharing.activity.MainActivity;
import com.example.phototsharing.entity.Image;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;
import com.example.phototsharing.adapter.ImageAdapter;
import com.example.phototsharing.entity.ImageBean;
import com.example.phototsharing.entity.Share;
import com.example.phototsharing.net.ApiInterface;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.PublishRepository;
import com.example.phototsharing.net.URLS;
import com.example.phototsharing.utilis.BaseResponse;
import com.example.phototsharing.utilis.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddShareFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USERID = "userId";

    Image image = new Image();
    Share shareInfo = new Share();
    private String titleSt;
    private String username;
    private long userId;
    List<Uri> imageUris = new ArrayList<>();
    List<File> imgFile = new ArrayList<>();
    private String agrs;

    private EditText title;
    private EditText content;
    private Button submitButton;
    private Button saveDraftButton;
    private GridView gridView;
    private String imageCode;  // 保存选中的图片路径
    private PublishRepository publishRepository;

    ImageAdapter adapter;

    //返回上传图片的唯一标识码
    private Callback imgCodeCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response)
                throws IOException {
            if (response.isSuccessful()) {

                final String body = response.body().string();
                Gson gson = new Gson();
                Log.d("info", body);
                BaseResponse<Image> res = gson.fromJson(body, new TypeToken<BaseResponse<Image>>() {
                }.getType());
                    image = res.getData();
                    shareInfo.setImageCode(image.imageCode);

                if (agrs.equals("add")){
                    addShare("add");
                }else {
                    addShare("save");
                }

            } else {
                Log.d("error", response.toString());
            }
        }
    };
    //新增图文分享返回
    private final Callback shareCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful()) {
                        Type jsonType = new TypeToken<BaseResponse<Object>>() {
                        }.getType();
                        // 获取响应体的json串
                        String body = null;
                        try {
                            body = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("info", body);
                        // 解析json串到自己封装的状态
                        Gson gson = new Gson();
                        BaseResponse<Object> dataResponseBody = gson.fromJson(body, jsonType);
                        Log.d("info", dataResponseBody.toString());
                        if(agrs.equals("add")){
                            Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                        }else{
//                            Toast.makeText(getActivity(), "存入草稿箱", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getActivity(),MainActivity.class);
                        }
                    }
                }
            });
        }
    };

    //底部弹窗,操作图片
    private void showBottomDialog(int position){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getContext());
        //2、设置布局
        View view = View.inflate(getContext(),R.layout.dialog_delete,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.getDecorView().setBackgroundColor(Color.WHITE);
        dialog.show();

        dialog.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUris.remove(position);
                imgFile.remove(position-1);
                adapter = new ImageAdapter(getContext(), imageUris);
                updateGridViwHeight();
                gridView.setAdapter(adapter);

                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



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
            titleSt = getArguments().getString(ARG_TITLE);
            username = getArguments().getString(ARG_USERNAME);
            userId = getArguments().getLong(ARG_USERID);
        }
        publishRepository = new PublishRepository(getContext());
        imageBean = new ImageBean(); // 初始化 ImageBean
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_publish, container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Uri localUri = Uri.parse("android.resource://com.example.phototsharing/" + R.drawable.add_pic_logo);
        imageUris.add(localUri);

        content = view.findViewById(R.id.text);
        title = view.findViewById(R.id.title);
        submitButton = view.findViewById(R.id.submit_button);
        saveDraftButton = view.findViewById(R.id.draft_button);
        gridView = view.findViewById(R.id.gridview); // 上传图片的

        //点击发布按钮
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                agrs = "add";
                if (imgFile.size() == 0) {
                    Toast.makeText(getContext(), "请添加图片!", Toast.LENGTH_LONG).show();
                } else if ((content.getText().toString()).isEmpty()) {
                    Toast.makeText(getContext(), "请填写内容!", Toast.LENGTH_LONG).show();
                } else if ((title.getText().toString()).isEmpty()) {
                    Toast.makeText(getContext(), "请填写标题!", Toast.LENGTH_LONG).show();
                } else {
                    upload();
                }
            }
        });

        //点击保存草稿按钮
        saveDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agrs="save";
                upload();
                Toast.makeText(getContext(), "草稿保存成功", Toast.LENGTH_SHORT).show();
                // 保存成功后可以导航到草稿页面
                Intent intent = new Intent(getContext(), DraftsActivity.class);
                startActivity(intent);
            }
        });
        // 假设你有一个从本地获取的 URI 列表
        adapter = new ImageAdapter(getContext(), imageUris);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理图标点击事件
                if (position == 0) {
                    // 如果点击了第一个位置（加号图标），执行添加图片的逻辑
                    addImg();
                }else {
                }
            }
        });

        // 点击草稿箱按钮并设置跳转 DraftsActivity 的点击事件
        Button draftButton = view.findViewById(R.id.draft);
        draftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Intent 跳转到 DraftsActivity
                Intent intent = new Intent(getContext(), DraftsActivity.class);
                startActivity(intent);
                agrs = "change";
            }
        });

    }

    //定义回调接口
    public interface UploadCallback {
        void onSuccess(ResponseBody imageBean);
        void onFailure(Throwable t);
    }

    //上传接口
    private void upload(){
        Log.d("upload","upload");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // url路径
                String url = "https://api-store.openguet.cn/api/member/photo/image/upload";

                // 请求头
                Headers headers = new Headers.Builder()
                        .add("appId", "946c9e7fc48c4929be6c146343abe1a3")
                        .add("appSecret", "072631dda00ac616b433f90418a2f271604f0")
                        .add("Accept", "application/json, text/plain, */*")
                        .build();

                // 构造请求体
                MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                // 添加多个文件到请求体中
                for (File file : imgFile) {
                    // 添加文件部分
                    requestBodyBuilder.addFormDataPart(
                            "fileList",  // 表单字段名
                            file.getName(),  // 文件名，可自定义
                            RequestBody.create(MediaType.parse("image/jpeg"), file)
                    );
                }

                // 构建请求体
                RequestBody requestBody = requestBodyBuilder.build();
                //请求组合创建
                Request request = new Request.Builder()
                        .url(url)
                        // 将请求头加至请求中
                        .headers(headers)
                        .post(requestBody)
                        .build();
                try {
                    OkHttpClient client = new OkHttpClient();
                    //发起请求，传入callback进行回调
                    client.newCall(request).enqueue(imgCodeCallback);
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }

            };
        }).start();;
    }

    //发布接口
    private void addShare(String way){
        new Thread(() -> {
            shareInfo.setContent(content.getText().toString());
            shareInfo.setTitle(title.getText().toString());
            shareInfo.setpUserId(userId);
            String url = "";
            // url路径
            if (way.equals("add")){
                url = "https://api-store.openguet.cn/api/member/photo/share/add";
            }else if(way.equals("change")){
                url = "https://api-store.openguet.cn/api/member/photo/share/change";
            } else if (way.equals("save")) {
                url = "https://api-store.openguet.cn/api/member/photo/share/save";
            }

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "946c9e7fc48c4929be6c146343abe1a3")
                    .add("appSecret", "072631dda00ac616b433f90418a2f271604f0")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串

            // 将Map转换为字符串类型加入请求体中
            Gson gson = new Gson();
            String body = gson.toJson(shareInfo);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(shareCallback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    // 打开系统相册选择图片
    private void addImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 允许多选
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，打开相册
                addImg();
            } else {
                // 权限被拒绝，给出提示
                Toast.makeText(getContext(), "需要读取存储权限以选择图片", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 处理选中图片后的逻辑
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    uri转base64
                    try {
                        // 打开输入流
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                        // 创建临时文件
                        File tempFile = createTempFile(inputStream);
                        // 将临时文件存储起来，例如添加到列表中
                        imgFile.add(tempFile);
                        // 关闭输入流
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUris.add(imageUri);


                }

            }
            updateGridViwHeight();
            gridView = getView().findViewById(R.id.gridview);
            // 假设你有一个从本地获取的 URI 列表
            adapter = new ImageAdapter(getContext(), imageUris);
            gridView.setAdapter(adapter);
        }
    }

    private File createTempFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp_image", ".jpg", getActivity().getCacheDir());
        OutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        return tempFile;
    }

    public interface PublishCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }

    private OkHttpClient client = new OkHttpClient(); // 初始化 OkHttpClient
    private Gson gson = new Gson(); // 初始化 Gson

    // 保存草稿
    private void saveDraft(String title, String content, String imageCode, long userId, PublishCallback callback) {
        try {
            // 这里可以使用 SharedPreferences 或数据库保存草稿信息
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("drafts", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("title", title);
            editor.putString("content", content);
            editor.putString("imageCode", imageCode);
            editor.putLong("userId", userId);
            editor.apply(); // 异步保存草稿

            // 保存成功时调用回调
            callback.onSuccess("草稿保存成功");
        } catch (Exception e) {
            // 如果保存时发生异常，调用错误回调
            callback.onError(e.getMessage());
        }
    }

    // 保存草稿按钮点击事件
//    private void onSaveDraftButtonClick() {
//        long userId = this.userId; // 用户ID
//        String imageCode = this.imageCode; // 获取图片代码
//
//        saveDraft(title.getText().toString(), content.getText().toString(), imageCode, userId, new PublishCallback() {
//            @Override
//            public void onSuccess(String message) {
//                Toast.makeText(getContext(), "草稿保存成功", Toast.LENGTH_SHORT).show();
//                // 保存成功后可以导航到草稿页面
//                Intent intent = new Intent(getContext(), DraftsActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Toast.makeText(getContext(), "草稿保存失败: " + errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // gridview自适应高度
    public void updateGridViwHeight(){
        int col = 4;// gridView.getNumColumns();
        int totalHeight = 0;
        // i每次加2，相当于adapter.getCount()小于等于2时 循环一次，计算一次item的高度， adapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < adapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = adapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += 266;
            Log.d("row", String.valueOf(i/col));
            Log.d("height", String.valueOf(totalHeight));
        }

        // 获取gridView的布局参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置参数
        gridView.setLayoutParams(params);
    }

}


