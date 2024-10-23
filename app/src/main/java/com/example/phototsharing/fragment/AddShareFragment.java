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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.example.phototsharing.MyInfo.EditMyInfo;
import com.example.phototsharing.R;
//import com.example.phototsharing.main.MainActivity;
import com.example.phototsharing.activity.MainActivity;
import com.example.phototsharing.net.ImgCodeCallback;
import com.example.phototsharing.net.ShareCallback;
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

    private static final String ARG_TITLE = "title";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USERID = "userId";

    private String title;
    private String username;
    private long userId;

    private EditText textContentEditText;
    private Button submitButton;

    public static AddShareFragment newInstance(String title, String myUserName, long myUserId) {
        AddShareFragment fragment = new AddShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_USERNAME, myUserName);
        args.putLong(ARG_USERID, myUserId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            username = getArguments().getString(ARG_USERNAME);
            userId = getArguments().getLong(ARG_USERID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_publish, container, false);

        textContentEditText = view.findViewById(R.id.text);
        submitButton = view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> publishContent());

        return view;
    }

    private void publishContent() {
        String content = textContentEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "正文不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "发布成功！", Toast.LENGTH_SHORT).show();
        }
    }

    // 接收 UploadFragment 传递的图片路径
    public void updateWithImage(String imagePath) {
        Log.d("AddShareFragment", "接收到图片路径: " + imagePath);
    }
}
