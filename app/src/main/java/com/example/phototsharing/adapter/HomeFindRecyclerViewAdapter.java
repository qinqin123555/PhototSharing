package com.example.phototsharing.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.TrueOrFalseCallback;

import de.hdodenhof.circleimageview.CircleImageView;

//设置首页子fragment中RecyclerView的适配器，用于江数据源中的数据绑定到RecyclerView的子项上

public class HomeFindRecyclerViewAdapter extends RecyclerView.Adapter<HomeFindRecyclerViewAdapter.ViewHolder> {
    private ShareBean myData;
    private Context myContext;
    private OnItemClickListener myListener;
    private long myUserId;


    public HomeFindRecyclerViewAdapter(Context myContext,long myUserId){
        this.myContext = myContext;
        this.myUserId = myUserId;
    }

/*
    public HomeFindRecyclerViewAdapter(ShareBean myData, Context myContext){
        this.myData = myData;
        this.myContext = myContext;
    }
*/

    /*创建ViewHolder对象，加载子项布局*/
    @NonNull
    @Override
    public HomeFindRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_find_list_item,parent,false);
        return new ViewHolder(view);
    }

//   将数据绑定到ViewHolder上的控件上

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull HomeFindRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        long shareId = myData.getData().getRecords().get(position).getid();


        if (!myData.getData().getRecords().get(position).getImageUrlList().isEmpty()){
            Glide.with(myContext).load(myData.getData().getRecords().get(position).getImageUrlList().get(0)).into(holder.itemImage);
        } else{
            holder.itemImage.setImageResource(R.drawable.default_image);
        }

        holder.itemTitle.setText(myData.getData().getRecords().get(position).getTitle());
        Glide.with(myContext)
                .load(myData.getData().getRecords().get(position).getAvatar())
                .into(holder.itemAvatar);
        holder.itemUsername.setText(myData.getData().getRecords().get(position).getUsername());
        holder.itemLikeImage.setSelected(myData.getData().getRecords().get(position).getHasLike());
        Log.d(String.valueOf(myData.getData().getRecords().get(position).getid()),String.valueOf(myData.getData().getRecords().get(position).getHasLike()));
        holder.itemLikeNumber.setText(String.format("%d", myData.getData().getRecords().get(position).getLikeNum()));







//        点赞事件监听
        holder.itemLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myData.getData().getRecords().get(position).getHasLike()) {
                    MyRequest.cancelLikeRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            holder.itemLikeImage.setSelected(b);
                            myData.getData().getRecords().get(position).setHasLike(b);
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = holder.itemLikeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount--; // 对长整型值进行加法运算
                            holder.itemLikeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已取消点赞", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, "无法取消点赞", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    MyRequest.setLikeRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            holder.itemLikeImage.setSelected(b);
                            myData.getData().getRecords().get(position).setHasLike(b);
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = holder.itemLikeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount++; // 对长整型值进行加法运算
                            holder.itemLikeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已点赞", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, "无法点赞", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
//获得数据源的数量
    @Override
    public int getItemCount() {
        if (myData == null){
            return 0;
        }
        return myData.getData().getRecords().size();
    }


    public ShareBean getShareBean() {
        return myData;
    }


    public void changeAdapter(ShareBean myData, Context myContext){
        this.myData = myData;
        this.myContext = myContext;
    }

    //ViewHolder类，用于缓存子项布局中的控件
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;
        public CircleImageView itemAvatar;
        public TextView itemUsername;
        public ImageView itemLikeImage;
        public TextView itemLikeNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemAvatar = itemView.findViewById(R.id.item_profile_picture);
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
