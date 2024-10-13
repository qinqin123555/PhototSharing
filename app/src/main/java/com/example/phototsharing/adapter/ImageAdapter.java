package com.example.phototsharing.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Uri> mImageUris; // 改为 Uri 类型的数据源

    public ImageAdapter(Context context, List<Uri> imageUris) {
        mContext = context;
        mImageUris = imageUris;
    }

    @Override
    public int getCount() {
        return mImageUris.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageUris.get(position);
    }

    public void removeItem(int position) {mImageUris.remove(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250)); // 设置每个图片的宽高
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // 加载图片到 ImageView
        Glide.with(mContext)
                .load(mImageUris.get(position))
//                .placeholder(R.drawable.placeholder) // 加载中显示的占位图
//                .error(R.drawable.error) // 加载失败显示的图片
                .into(imageView);

        return imageView;
    }
}