package com.example.phototsharing.fragment;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.example.phototsharing.MyInfo.EditMyInfo;
import com.example.phototsharing.R;
//import com.example.phototsharing.main.MainActivity;
import com.example.phototsharing.activity.MainActivity;
import com.example.phototsharing.utilis.BaseResponse;
import com.example.phototsharing.utilis.Constants;
import com.example.phototsharing.adapter.ImageAdapter;
import com.example.phototsharing.entity.Share;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddShareFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private Button button;
    private TextView addShare;
    private TextView saveOrDelete;
    private EditText title;
    private EditText content;
    Image image = new Image();
    Share shareInfo = new Share();
    List<Uri> imageUris = new ArrayList<>();
    List<File> imgFile = new ArrayList<>();
    GridView gridView;
    ImageAdapter adapter;
    String imageCode;
    Bundle args;
    private SharedPreferences sharedPreferences;

    //返回上传图片的唯一标识码
    private Callback imgCodeCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "Failed to connect server!");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response)
                throws IOException {
            if (response.isSuccessful()) {

                final String body = response.body().string();
                        Gson gson = new Gson();
                        //Log.d("info", body);
                        BaseResponse<Image> res = gson.fromJson(body, new TypeToken<BaseResponse<Image>>() {
                        }.getType());
//                        Log.d("info", response.toString());
                if (res.getCode() == 500){
                    // 在非UI线程中执行时
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getContext(), "服务器错误!", Toast.LENGTH_SHORT).show();
                    });

                    ;
                    return;
                }else {
                    image = res.getData();
                    Log.d("data", image.getImageCode().toString());

                    shareInfo.setImageCode(image.imageCode);
                    Log.d("shareInfo",shareInfo.toString());
                    if (args == null){
                        addShare("add");
                    }else {
                        addShare("change");
                    }
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
            if (response.isSuccessful()){
                Type jsonType = new TypeToken<BaseResponse<Object>>(){}.getType();
                // 获取响应体的json串
                String body = response.body().string();
                Log.d("info", body);
                // 解析json串到自己封装的状态
                Gson gson = new Gson();
                BaseResponse<Object> dataResponseBody = gson.fromJson(body,jsonType);
                Log.d("info", dataResponseBody.toString());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 替换这里的布局为你的Fragment布局
        return inflater.inflate(R.layout.fragment_add_share, container, false);

    }

    @Override//页面初始化
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Uri localUri = Uri.parse("android.resource://com.example.phototsharing/" + R.drawable.add_pic_logo);
        imageUris.add(localUri);

        saveOrDelete = getView().findViewById(R.id.saveOrDelete);
        title = getView().findViewById(R.id.share_title);
        content = getView().findViewById(R.id.share_content);
        gridView = getView().findViewById(R.id.gridview);
        addShare = getView().findViewById(R.id.share);

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        shareInfo.setpUserId(sharedPreferences.getLong("id",0));
        //如果从草稿跳转,则初始化数据
        args = getArguments();
        if (args != null) {
            saveOrDelete.setText("删除");
            String[] image = args.getStringArray("urlList");
            List<String> urlStrings = Arrays.asList(image);
            String title_arg = args.getString("title");
            String content_arg = args.getString("content");
            if (urlStrings != null) {
                for (String urlString : urlStrings) {
                    imageUris.add(Uri.parse(urlString));
                    Log.d("44444444", urlString);
                    try {
                        // 打开输入流
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(urlString));
                        Log.d("11111111", "onViewCreated: ");
                        // 创建临时文件
                        File tempFile = createTempFile(inputStream);
                        Log.d("22222222", "onViewCreated: ");
                        // 将临时文件存储起来，例如添加到列表中
                        imgFile.add(tempFile);
                        Log.d("tempfile", tempFile.toString());
                        // 关闭输入流
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            content.setText(content_arg);
            title.setText(title_arg);
            // Use urlList, title, and content as needed
        }

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


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    showBottomDialog(position);
                }
                return true;
            }
        });

        addShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                shareInfo.setContent(String.valueOf(content.getText()));
                shareInfo.setTitle(String.valueOf(title.getText()));
                if (imgFile.size() == 0) {
                    Toast.makeText(getContext(), "请添加图片!", Toast.LENGTH_SHORT).show();
                } else if (shareInfo.getContent() == null) {
                    Toast.makeText(getContext(), "请填写标题!", Toast.LENGTH_SHORT).show();
                } else if (shareInfo.getTitle() == null) {
                    Toast.makeText(getContext(), "请填写内容!", Toast.LENGTH_SHORT).show();
                } else {
                    upload();
                }
            }
        });
        saveOrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long userId = sharedPreferences.getLong("id",0);
                if (args == null){
                    addShare("save");
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder (getActivity());//通过AlertDialog.Builder创建出一个AlertDialog的实例

                    dialog.setTitle("删除草稿");//设置对话框的标题
                    dialog.setMessage("确定要删除吗?");//设置对话框的内容
                    dialog.setCancelable(false);//设置对话框是否可以取消
                    dialog.setPositiveButton("确定", new DialogInterface. OnClickListener() {//确定按钮的点击事件

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Long shareId = Long.valueOf(args.getString("shareId"));
                            deleteShare(shareId,userId);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface. OnClickListener() {//取消按钮的点击事件
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();//显示对话框


                }
            }
        });
//        button = getView().findViewById(R.id.bt_upload);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upload();
//            }
//        });
    }
    //添加图片,跳转相册
    public void addImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 允许多选
        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_PICK_IMAGE);
    }
    //处理从相册获取的图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
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
    // 创建临时文件并写入数据
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

    //    上传文件请求,获取一组图片唯一标识码
    private void upload(){
        Log.d("upload","upload");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // url路径
                String url = "https://api-store.openguet.cn/api/member/photo/image/upload";

                // 请求头
                Headers headers = new Headers.Builder()
                        .add("appId", Constants.USER_APPID)
                        .add("appSecret", Constants.USER_APPSECRET)
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
    private void addShare(String way){
        new Thread(() -> {

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
                    .add("appId", Constants.USER_APPID)
                    .add("appSecret", Constants.USER_APPSECRET)
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
    private void deleteShare(Long shareId,Long userId){
        new Thread(() -> {

            // url路径
            String url = "https://api-store.openguet.cn/api/member/photo/share/delete?shareId="+shareId+"&userId="+userId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", Constants.USER_APPID)
                    .add("appSecret", Constants.USER_APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Gson gson = new Gson();
            Type jsonType = new TypeToken<BaseResponse<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            BaseResponse<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
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

//    //URL转URI
//    public URI toURI() throws URISyntaxException {
//        URI uri = new URI(toString());
//        if (authority != null && isBuiltinStreamHandler(handler)) {
//            String s = IPAddressUtil.checkAuthority(this);
//            if (s != null) throw new URISyntaxException(authority, s);
//        }
//        return uri;
//    }

    // 创建一个新的实例并设置参数
//    public static AddShareFragment newInstance(String[] urlList , String title , String content , String imageCode , String shareId)
      public static AddShareFragment newInstance(String title, String myUserName, long myUserId){
        AddShareFragment fragment = new AddShareFragment();
        Bundle args = new Bundle();
//        args.putStringArray("urlList", urlList);
//        args.putString("content", content);
//        args.putString("imageCode",imageCode);
//        args.putString("shareId",shareId);

          args.putString("title", title);
          args.putString("myUserName", myUserName);
          args.putLong("myUserId", myUserId);


        fragment.setArguments(args);
        return fragment;
    }

}
