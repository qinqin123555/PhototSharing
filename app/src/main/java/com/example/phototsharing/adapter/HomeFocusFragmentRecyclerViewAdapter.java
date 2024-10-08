package com.example.phototsharing.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.phototsharing.R;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.HasFocusBean;
import com.example.phototsharing.net.CommentCallback;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.TrueOrFalseCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFocusFragmentRecyclerViewAdapter extends RecyclerView.Adapter<HomeFocusFragmentRecyclerViewAdapter.ViewHolder> {
    private Context myContext;
    private HasFocusBean hasFocusBean;
    private long myUserId;

    public HomeFocusFragmentRecyclerViewAdapter(Context context) {
        this.myContext = context;
    }


    public void changeAdapter(Context context, HasFocusBean hasFocusBean, long myUserId) {
        this.hasFocusBean = hasFocusBean;
        this.myContext = context;
        this.myUserId = myUserId;
    }


    @NonNull
    @Override
    public HomeFocusFragmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_focus_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFocusFragmentRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (!hasFocusBean.getData().getRecords().get(position).getImageUrlList().isEmpty()){
            HomeChildDetailImageAdapter homeChildDetailImageAdapter = new HomeChildDetailImageAdapter(
                    myContext,
                    hasFocusBean.getData().getRecords().get(position).getImageUrlList());
            holder.imgViewPager2.setAdapter(homeChildDetailImageAdapter);

        }


        Glide.with(myContext).load(hasFocusBean.getData().getRecords().get(position).getAvatar()).into(holder.itemAvatar);
        holder.itemUserName.setText(hasFocusBean.getData().getRecords().get(position).getUsername());
        holder.itemLikeImage.setSelected(hasFocusBean.getData().getRecords().get(position).getHasLike());
        holder.itemLikeNum.setText(String.valueOf(hasFocusBean.getData().getRecords().get(position).getLikeNum()));
        holder.itemCollect.setSelected(hasFocusBean.getData().getRecords().get(position).getHasCollect());
        holder.itemCollectNum.setText(String.valueOf(hasFocusBean.getData().getRecords().get(position).getCollectNum()));
        holder.itemTitle.setText(hasFocusBean.getData().getRecords().get(position).getTitle());
        holder.itemContent.setText(hasFocusBean.getData().getRecords().get(position).getContent());

        MyRequest.getFirstCommentData(hasFocusBean.getData().getRecords().get(position).getid(), new CommentCallback() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(CommentBean commentBean) {
                String currentNum = String.valueOf(commentBean.getData().getTotal());
                /*long totalNum = Long.parseLong(currentNum);
                for (int i = 0; i < commentBean.getData().getTotal(); i++) {
                    MyRequest.getSecondCommentData(hasFocusBean.getData().getRecords().get(position).getid(), commentBean.getData().getRecords().get(i).getid(), new CommentCallback() {
                        @Override
                        public void onSuccess(CommentBean commentBean) {
                            String currentNum = String.valueOf(commentBean.getData().getTotal());
                            long num = Long.parseLong(currentNum);
                            totalNum = totalNum +num;
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
*/
                holder.itemCommentNum.setText(currentNum);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("TAG","无评论");

            }
        });


        holder.imgViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        holder.imgViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        setOnLikeClickListener(holder.itemLikeImage, holder.itemLikeNum, position);

        setOnCollectClickListener(holder.itemCollect,holder.itemCollectNum,position);
        setOnCommentAreaOnClickListener(holder.itemCommentArea,position);
    }

    @Override
    public int getItemCount() {
        if (hasFocusBean != null && hasFocusBean.getData() != null){
            return hasFocusBean.getData().getRecords().size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView itemAvatar;
        public TextView itemUserName;
        public ViewPager2 imgViewPager2;
        public ImageView itemLikeImage;
        public TextView itemLikeNum;
        public ImageView itemCollect;
        public TextView itemCollectNum;
        public TextView itemTitle;
        public TextView itemContent;
        public ImageView itemCommentArea;
        public TextView itemCommentNum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemAvatar = itemView.findViewById(R.id.iv_home_focus_avatar);
            itemUserName = itemView.findViewById(R.id.tv_home_focus_username);
            imgViewPager2 = itemView.findViewById(R.id.focus_image_viewpager);
            itemLikeImage = itemView.findViewById(R.id.iv_home_focus_like);
            itemLikeNum = itemView.findViewById(R.id.tv_home_focus_like_number);
            itemCollect = itemView.findViewById(R.id.iv_home_focus_collect);
            itemCollectNum = itemView.findViewById(R.id.tv_home_focus_collect_number);
            itemTitle = itemView.findViewById(R.id.tv_home_focus_title);
            itemContent = itemView.findViewById(R.id.tv_home_focus_content);
            itemCommentArea = itemView.findViewById(R.id.iv_home_focus_comment);
            itemCommentNum = itemView.findViewById(R.id.tv_home_focus_comment_number);
        }
    }

    private void setOnLikeClickListener(ImageView like, TextView likeNumber, int position) {
        like.setOnClickListener(new View.OnClickListener() {
            long shareId = hasFocusBean.getData().getRecords().get(position).getid();

            @Override
            public void onClick(View view) {
                if (hasFocusBean.getData().getRecords().get(position).getHasLike()) {
                    MyRequest.cancelLikeRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            like.setSelected(b);
                            hasFocusBean.getData().getRecords().get(position).setHasLike(b);                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = likeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount--; // 对长整型值进行加法运算
                            likeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已取消点赞", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    MyRequest.setLikeRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            like.setSelected(b);
                            hasFocusBean.getData().getRecords().get(position).setHasLike(b);                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = likeNumber.getText().toString(); // 获取 TextView 中的文本
                            long likeCount = Long.parseLong(currentText); // 将文本转换为长整型
                            likeCount++; // 对长整型值进行加法运算
                            likeNumber.setText(String.valueOf(likeCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已点赞", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void setOnCollectClickListener(ImageView collect, TextView collectNumber, int position) {
        long shareId = hasFocusBean.getData().getRecords().get(position).getid();
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasFocusBean.getData().getRecords().get(position).getHasCollect()) {
                    MyRequest.cancelCollectRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            collect.setSelected(b);
                            hasFocusBean.getData().getRecords().get(position).setHasCollect(b);
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = collectNumber.getText().toString(); // 获取 TextView 中的文本
                            long collectCount = Long.parseLong(currentText); // 将文本转换为长整型
                            collectCount--; // 对长整型值进行加法运算
                            collectNumber.setText(String.valueOf(collectCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已取消收藏", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, (CharSequence) throwable, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    MyRequest.setCollectRequest(shareId, myUserId, new TrueOrFalseCallback() {
                        @Override
                        public void onSuccess(Boolean b) {
                            collect.setSelected(b);
                            hasFocusBean.getData().getRecords().get(position).setHasCollect(b);
                            // 假设 likeNumber 是一个 TextView，并且它当前显示的是一个数字字符串
                            String currentText = collectNumber.getText().toString(); // 获取 TextView 中的文本
                            long collectCount = Long.parseLong(currentText); // 将文本转换为长整型
                            collectCount++; // 对长整型值进行加法运算
                            collectNumber.setText(String.valueOf(collectCount)); // 将结果设置回 TextView
                            Toast.makeText(myContext, "已加入收藏列表", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(myContext, "无法加入收藏列表", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }


    private void setOnCommentAreaOnClickListener (ImageView commentArea, int position) {
        commentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               initDialogCommentArea(hasFocusBean.getData().getRecords().get(position).getid());
            }
        });
    }

    private void initDialogCommentArea(long shareId) {
        // 创建AlertDialog.Builder
        BottomSheetDialog dialog = new BottomSheetDialog(myContext);
        // 设置对话框的布局
        LayoutInflater inflater = LayoutInflater.from(myContext);
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_focus_comment, null);
        dialog.setContentView(dialogView);

        // 查找EditText
        TextView commentTotalNum = dialogView.findViewById(R.id.focus_total_num_text);
        ImageView dialogClose = dialogView.findViewById(R.id.dialog_close_button);
        RecyclerView commentListview = dialogView.findViewById(R.id.focus_dialog_comment_recycler_view);
        EditText commentEdit = dialogView.findViewById(R.id.home_child_detail_comment_edit_text);

        MyRequest.getFirstCommentData(shareId, new CommentCallback() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(CommentBean commentBean) {
                commentTotalNum.setText(String.format("共%d条评论", commentBean.getData().getTotal()));
                HomeChildDetailFirstCommentAdapter homeChildDetailFirstCommentAdapter = new HomeChildDetailFirstCommentAdapter(commentBean, myContext);
                commentListview.setAdapter(homeChildDetailFirstCommentAdapter);
                commentListview.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.VERTICAL,false));
                commentListview.addItemDecoration(new DividerItemDecoration(myContext,DividerItemDecoration.VERTICAL));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("TAG","无评论");

            }
        });

        dialog.show();

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}
