package com.example.phototsharing.adapter;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.ShareBean;

import java.util.List;

//设置首页子fragment中RecyclerView的适配器，用于江数据源中的数据绑定到RecyclerView的子项上

public class HomeChildRecyclerViewAdapter extends RecyclerView.Adapter<HomeChildRecyclerViewAdapter.ViewHolder> {
    private ShareBean myData;
    private Context myContext;
    private OnItemClickListener myListener;

    public HomeChildRecyclerViewAdapter(Context context){
        this.myContext = context;
    }

    public HomeChildRecyclerViewAdapter(ShareBean myData, Context myContext){
        this.myData = myData;
        this.myContext = myContext;
    }

    /*创建ViewHolder对象，加载子项布局*/
    @NonNull
    @Override
    public HomeChildRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_child_list_item,parent,false);
        return new ViewHolder(view);
    }

//   将数据绑定到ViewHolder上的控件上

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull HomeChildRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.itemTitle.setText(myData.getData().getRecords().get(position).getTitle());
        holder.itemUsername.setText(myData.getData().getRecords().get(position).getUsername());
        holder.itemLikeNumber.setText(String.format("%d", myData.getData().getRecords().get(position).getLikeNum()));

        List<String> imgUrl = myData.getData().getRecords().get(position).getImageUrlList();

        if (imgUrl.get(0) != null){
            Glide.with(myContext).load(imgUrl.get(0)).into(holder.itemImage);
        } else{
            holder.itemImage.setImageResource(R.drawable.default_image);
        }



    }
//获得数据源的数量
    @Override
    public int getItemCount() {
        if (myData == null){
            return 0;
        }
        return myData.getData().getRecords().size();
    }

    public void changeAdapter(ShareBean myData, Context myContext){
        this.myData = myData;
        this.myContext = myContext;
    }

    //ViewHolder类，用于缓存子项布局中的控件
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemUsername;
        public ImageView itemLikeImage;
        public TextView itemLikeNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemUsername = itemView.findViewById(R.id.item_username);
            itemLikeImage = itemView.findViewById(R.id.item_like_image);
            itemLikeNumber = itemView.findViewById(R.id.item_like_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 触发回调
                    if (myListener != null) {
                        int position = getAdapterPosition(); // 注意这个可能返回RecyclerView.NO_POSITION
                        if (position != RecyclerView.NO_POSITION) {
                            myListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }

    //子项监听
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.myListener = listener;
    }

}
