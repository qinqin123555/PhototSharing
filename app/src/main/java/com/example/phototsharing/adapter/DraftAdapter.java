package com.example.phototsharing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide; // 使用 Glide 加载图片
import com.example.phototsharing.R;
import com.example.phototsharing.entity.ImageBean;

import java.util.List;

public class DraftAdapter extends BaseAdapter {

    private List<ImageBean> drafts; // 使用 ImageBean 类型的草稿列表
    private Context context;

    public DraftAdapter(Context context, List<ImageBean> drafts) {
        this.context = context;
        this.drafts = drafts;
    }

    @Override
    public int getCount() {
        return drafts.size();
    }

    @Override
    public Object getItem(int position) {
        return drafts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_draft, parent, false);
        }

        // 绑定视图
        TextView titleView = convertView.findViewById(R.id.draft_title);
        TextView contentView = convertView.findViewById(R.id.draft_content);
        ImageView imageView = convertView.findViewById(R.id.draft_image);

        // 获取当前草稿 (ImageBean)
        ImageBean draft = (ImageBean) getItem(position);

        // 设置标题和正文，假设使用 ImageBean 的 msg 字段作为标题
//        titleView.setText(draft.getMsg());
//        if (draft.getData() != null && !draft.getData().getImageUrlList().isEmpty()) {
//            // 获取第一张图片的 URL
//            String imageUrl = draft.getData().getImageUrlList().get(0);  // 直接使用字符串
//            Glide.with(context)
//                    .load(imageUrl)  // 直接加载 URL 字符串
//                    .into(imageView);
//        } else {
//            // 如果没有图片，设置默认图片或隐藏 ImageView
//            imageView.setImageResource(R.drawable.default_image);
//        }

        return convertView;
    }
}
