package com.example.phototsharing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototsharing.R;

import java.util.List;

public class HomeChildDetailImageAdapter extends RecyclerView.Adapter<HomeChildDetailImageAdapter.ViewHolder> {
    private List<String> imgUrls;
    private Context myContext;

    public HomeChildDetailImageAdapter(Context context,List<String> imgUrlList){
        this.imgUrls = imgUrlList;
        this.myContext = context;
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
        Glide.with(myContext).load(imgUrls.get(position)).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        if (imgUrls == null){
            return 0;
        }
        return imgUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.home_child_detail_image);

        }
    }

    public void changeAdapter(Context context,List<String> imgUrls){
        this.imgUrls = imgUrls;
        this.myContext = context;
    }

}
