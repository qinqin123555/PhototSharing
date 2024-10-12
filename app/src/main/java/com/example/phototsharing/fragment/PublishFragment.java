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

    private EditText subTitleEditText;
    private EditText textContentEditText;
    private Button submitButton;

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        // Initialize views
        subTitleEditText = view.findViewById(R.id.sub_title);
        textContentEditText = view.findViewById(R.id.text);
        submitButton = view.findViewById(R.id.submit_button_bottom);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(v -> publishContent());

        return view;
    }

    private void publishContent() {
        String title = subTitleEditText.getText().toString().trim();
        String content = textContentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            // Log the post content, here you can handle the posting logic
            Log.d("PublishFragment", "Title: " + title);
            Log.d("PublishFragment", "Content: " + content);
            Toast.makeText(getContext(), "发布成功！", Toast.LENGTH_SHORT).show();

            // TODO: Implement logic to send the title and content to your server
        }
    }
}