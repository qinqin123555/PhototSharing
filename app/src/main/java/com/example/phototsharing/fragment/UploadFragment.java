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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phototsharing.R;

public class UploadFragment extends Fragment {

    private ImageView uploadImageView;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        uploadImageView = view.findViewById(R.id.custom_image);
        uploadImageView.setOnClickListener(v -> openImagePicker());
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
            mListener.onImageSelected(imagePath);  // 调用传递图片路径的方法
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
