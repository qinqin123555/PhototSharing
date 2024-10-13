package com.example.phototsharing.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;

public class PublishFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USERID = "userId";

    private String title;
    private String username;
    private long userId;

    private EditText textContentEditText;
    private Button submitButton;

    public static PublishFragment newInstance(String title, String username, long userId) {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_USERNAME, username);
        args.putLong(ARG_USERID, userId);
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
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

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
            Log.d("PublishFragment", "发布内容: " + content);
            Log.d("PublishFragment", "发布者: " + username + " (ID: " + userId + ")");
            Toast.makeText(getContext(), "发布成功！", Toast.LENGTH_SHORT).show();
        }
    }

    // 接收 UploadFragment 传递的图片路径
    public void updateWithImage(String imagePath) {
        Log.d("PublishFragment", "接收到图片路径: " + imagePath);
    }
}
