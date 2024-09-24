package com.example.phototsharing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.fastjson.JSON;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.Code;
import com.example.phototsharing.net.MyHeaders;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.URLS;
import com.example.phototsharing.utilis.KeyBoardUtil;
import com.example.phototsharing.utilis.MergeURLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    //密码可见转换
    private Boolean eyeSwitch = false;

    //账户密码初始化
    private String account = "";
    private String password = "";

    //登录用户列表
    Map<String, String> loginInfo = new HashMap<>();

    private Handler handler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //按钮
        final Button btnLogin = findViewById(R.id.login);
        // 注册按钮
        final TextView btnRegister = findViewById(R.id.button_register);

        // 获取图片资源
        // account
        final ImageView accountView = findViewById(R.id.account_img);
        final ImageView accountClose = findViewById(R.id.account_close);

        // pwd
        final ImageView passwordView = findViewById(R.id.password_img);
        final ImageView passwordClose = findViewById(R.id.password_close);
        final ImageView passwordEye = findViewById(R.id.password_eye);

        // input
        EditText accountText = findViewById(R.id.login_account);
        EditText passwordText = findViewById(R.id.login_password);

        // clear
        clearEditText(accountText, accountClose);
        clearEditText(passwordText, passwordClose);

        //对账户输入监听
        accountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int num, int num1, int num2) {
                accountClose.setVisibility(View.GONE);//隐藏
                accountView.setVisibility(View.VISIBLE);//显示
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int num, int num1, int num2) {
                accountClose.setVisibility(View.VISIBLE);
                accountView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    accountClose.setVisibility(View.VISIBLE);
                    accountView.setVisibility(View.GONE);
                    account = editable.toString();
                } else {
                    accountClose.setVisibility(View.GONE);
                    accountView.setVisibility(View.VISIBLE);
                }
            }
        });

        //对密码输入监听
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordClose.setVisibility(View.GONE);
                passwordView.setVisibility(View.VISIBLE);
                passwordEye.setVisibility(View.GONE);
                eyeSwitch = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordClose.setVisibility(View.VISIBLE);
                passwordView.setVisibility(View.GONE);
                passwordEye.setVisibility(View.VISIBLE);
            }

            @Override

            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    passwordClose.setVisibility(View.VISIBLE);
                    passwordView.setVisibility(View.GONE);
                    passwordEye.setVisibility(View.VISIBLE);
                    eyeSwitch = false;
                    password = editable.toString();
                } else {
                    passwordClose.setVisibility(View.GONE);
                    passwordView.setVisibility(View.VISIBLE);
                    passwordEye.setVisibility(View.GONE);
                }
            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                PersonBean personBean = (PersonBean) msg.obj;

                if (personBean.getCode() == Code.SUCCESS) {
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(personBean.getData()));
                    if (SharedPreferencesUtil.putMap(LoginActivity.this, map)) {
                        Toast.makeText(LoginActivity.this, "图跃，分享你的生活", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, NavBottomActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("ERROR", "ERROR Login");
                    }
                } else {
                    Toast.makeText(LoginActivity.this, personBean.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 登录逻辑
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!account.equals("") && !password.equals("")) {
                    loginInfo.put("username", account);
                    loginInfo.put("password", password);


                    MyRequest MyRequest = new MyRequest();
                    MyRequest.doPost(MergeURLUtil.merge(URLS.LOGIN.getUrl(), loginInfo),
                            MyHeaders.getHeaders(),
                            new HashMap<>(),
                            new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    if (response.isSuccessful()) {
                                        PersonBean personBean = new Gson().fromJson(response.body().string(), PersonBean.class);
                                        Message msg = new Message();
                                        msg.obj = personBean;
                                        handler.sendMessage(msg);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*
注册按钮监听，跳转
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

*/

        //眼睛展示功能
        passwordEye.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    passwordEye.setImageResource(R.drawable.open_eye);
                    passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    passwordEye.setImageResource(R.drawable.closed_eye);
                    passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passwordText.setTypeface(Typeface.DEFAULT);
                    break;
                }
                default: break;
            }

            // 光标判断
            CharSequence charSequence = passwordText.getText();
            if (charSequence != null) {
                Spannable spannable = (Spannable) charSequence;
                Selection.setSelection(spannable, charSequence.length());
            };
            return true;
        });

    }

    //点击删除EditText内容功能
    private void clearEditText(EditText accountText, ImageView accountClose) {
        accountClose.setOnClickListener(v->accountText.setText(""));
    }

    // 点击外部事件，关闭键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 当触摸事件是按下操作时
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的视图
            View v = getCurrentFocus();
            // 如果需要隐藏输入法（即点击在 EditText 控件外部）
            if (isShouldHideInput(v, ev)) {
                // 获取输入法管理器
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 断言 v 不为空，确保 v 已经被赋值
                    assert v != null;
                    // 关闭软键盘，KeyBoardUtil 是一个工具类，用于操作软键盘
                    KeyBoardUtil.closeKeyboard(v);
                }
            }
            // 将事件传递给父类进行默认处理
            return super.dispatchTouchEvent(ev);
        }
        // 对于其他类型的触摸事件，将事件传递给窗口进行默认处理，或者调用 onTouchEvent 进行处理
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent ev) {
        // 如果当前焦点视图不为空且是 EditText 类型
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的屏幕位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 判断触摸事件是否在输入框内部，如果不在输入框内部，则返回 true，表示需要隐藏输入法
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        // 如果当前焦点视图为空或不是 EditText 类型，返回 false，不需要隐藏输入法
        return false;
    }

}