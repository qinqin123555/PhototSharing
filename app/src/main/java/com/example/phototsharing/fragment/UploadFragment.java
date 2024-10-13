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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;

public class UploadFragment extends Fragment {

    private ImageView imageView;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 为该片段填充布局
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        // 获取布局中的 ImageView 和 LinearLayout
        imageView = view.findViewById(R.id.custom_image);
        LinearLayout uploadLayout = view.findViewById(R.id.upload_layout);

        // 初始化图片选择器启动器
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当选择图片的结果返回时执行此处代码
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            // 将选择的图片设置到 ImageView 中显示
                            imageView.setImageURI(imageUri);
                        }
                    }
                }
        );

        // 设置点击监听器，点击时打开图片选择器
        uploadLayout.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void openImagePicker() {
        // 创建一个 Intent 来打开系统相册，选择图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 只显示图片类型
        pickImageLauncher.launch(intent); // 启动选择器
    }


    private OnUploadFragmentInteractionListener mListener;

    // 定义接口
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

    // 选择图片时调用
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
