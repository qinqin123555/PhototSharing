package com.example.phototsharing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;

public class UploadFragment extends Fragment {

    private ImageView imageView;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 为该片段填充布局
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        // 获取 ImageView 和点击文本
        imageView = view.findViewById(R.id.custom_image);
        TextView uploadText = view.findViewById(R.id.upload_text);

        // 初始化图片选择器
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                    selectImage(imageUri.toString());
                }
            }
        });

        // 设置点击事件来打开图片选择器
        uploadText.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private OnUploadFragmentInteractionListener mListener;

    // 定义接口以通知 Activity 图片被选中
    public interface OnUploadFragmentInteractionListener {
        void onImageSelected(String imagePath);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUploadFragmentInteractionListener) {
            mListener = (OnUploadFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " 必须实现 OnUploadFragmentInteractionListener");
        }
    }

    private void selectImage(String imagePath) {
        if (mListener != null) {
            mListener.onImageSelected(imagePath);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
