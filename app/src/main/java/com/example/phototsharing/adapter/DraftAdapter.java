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

import java.util.Arrays;
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
        ViewHolder holder;

        // 使用新定义的 item_draft 布局文件
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_draft, parent, false);
            holder = new ViewHolder();
            holder.titleView = convertView.findViewById(R.id.draft_title);
            holder.contentView = convertView.findViewById(R.id.draft_content);
            holder.imageView = convertView.findViewById(R.id.draft_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageBean draft = (ImageBean) getItem(position);

        // 绑定草稿数据到标题和内容
        holder.titleView.setText(draft.getMsg());
        holder.contentView.setText(draft.getMsg());

        // 绑定图片，如果存在图片 URL 则加载图片
        if (draft.getData() != null && !draft.getData().getImageUrlList().isEmpty()) {
            String imageUrl = draft.getData().getImageUrlList().get(0); // 获取第一个图片 URL
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_default_image) // 设置默认图片占位符
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_default_image); // 没有图片时使用默认图片
        }

        return convertView;
    }


    // ViewHolder 模式优化性能
    private static class ViewHolder {
        TextView titleView;
        TextView contentView;
        ImageView imageView;
    }
}
