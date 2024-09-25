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


import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.net.ApiInterface;

import com.example.phototsharing.utilis.KeyBoardUtil;
import com.example.phototsharing.utilis.MergeURLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    //密码可见转换
    private Boolean eyeSwitch = false;

    //账户密码初始化
    private String username  = "";
    private String password = "";

    private Retrofit retrofit;

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

        initRetrofit(); // 初始化 Retrofit

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
                    username = editable.toString();
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

        // 登录逻辑
        // 登录逻辑
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appId = "946c9e7fc48c4929be6c146343abe1a3";
                String appSecret = "072631dda00ac616b433f90418a2f271604f0";
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                // 直接在请求中添加 username 和 password
                Call<PersonBean> call = apiInterface.login(appId, appSecret, username, password);

                call.enqueue(new Callback<PersonBean>() {
                    @Override
                    public void onResponse(Call<PersonBean> call, Response<PersonBean> response) {
                        Log.d("LoginActivity", "Response: " + response.body());
                        if (response.isSuccessful() && response.body() != null) {
                            PersonBean personBean = response.body();
                            // 处理登录成功的逻辑
                            Toast.makeText(LoginActivity.this, "登录成功: " + personBean.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            // 处理登录失败的逻辑
                            Toast.makeText(LoginActivity.this, "登录失败: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PersonBean> call, Throwable t) {
                        Log.e("LoginActivity", "Login failed: " + t.getMessage());
                        // 处理请求失败的逻辑
                        Toast.makeText(LoginActivity.this, "请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/") // 设置基础 URL
                .addConverterFactory(GsonConverterFactory.create()) // 添加 GSON 转换器
                .build();
    }

}