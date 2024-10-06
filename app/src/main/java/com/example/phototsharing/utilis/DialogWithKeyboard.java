package com.example.phototsharing.utilis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phototsharing.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DialogWithKeyboard {    // 持有回调接口的引用
    private final OnSendClickListener listener;

    // 构造方法或设置监听器的方法
    public DialogWithKeyboard(OnSendClickListener listener) {
        this.listener = listener;
    }



    // 定义回调接口
    public interface OnSendClickListener {
        void onSendClick(String content);
    }

    public void showDialogWithKeyboard(Context context) {
        // 创建AlertDialog.Builder
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        // 设置对话框的布局
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_edit_input, null);
        dialog.setContentView(dialogView);

        // 查找EditText
        EditText editText = dialogView.findViewById(R.id.comment_input_edit);
        Button send = dialogView.findViewById(R.id.comment_send_Button);

//        接收输入的内容

        // 创建并显示对话框
        // 设置对话框显示的监听器
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface di) {
                // 确保EditText获得焦点
                editText.requestFocus();
                // 请求显示键盘
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputContent = editText.getText().toString();
                if (!inputContent.isEmpty()) {
                    if (listener != null) {
                        listener.onSendClick(inputContent);
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(context,"评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 显示对话框
        dialog.show();

    }

}
