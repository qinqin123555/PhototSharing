package com.example.phototsharing.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.phototsharing.R;

import java.util.List;

public class HomeChildDetailImageAdapter extends RecyclerView.Adapter<HomeChildDetailImageAdapter.ViewHolder> {

    private List<String> imgUrls;
    private Context context;


    public HomeChildDetailImageAdapter(){

    }

    public HomeChildDetailImageAdapter(Context context,List<String> imgUrls){
        this.imgUrls = imgUrls;
        this.context = context;
    }

    public void changeAdapter(Context context,List<String> imgUrls){
        this.imgUrls = imgUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeChildDetailImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_child_detail_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChildDetailImageAdapter.ViewHolder holder, int position) {
        if (imgUrls != null && position <= imgUrls.size()) {
            Glide.with(context).load(imgUrls.get(position)).into(holder.homeChildDetailImage);
        } else {
            Log.d("ImageAdapter", "图片为空");
        }
    }

    @Override
    public int getItemCount() {
        if (imgUrls != null){
            return imgUrls.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView homeChildDetailImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            homeChildDetailImage = itemView.findViewById(R.id.home_child_detail_image);
        }
    }
}
