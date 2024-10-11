package com.example.phototsharing.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.phototsharing.R;
import com.example.phototsharing.entity.RegisterBean;
import com.example.phototsharing.net.MyRequest;
import com.example.phototsharing.net.RegisterCallback;
import com.example.phototsharing.utilis.KeyBoardUtil;

import java.util.logging.Handler;

public class RegisterActivity extends AppCompatActivity {

    private String account; // 用户账号
    private String password; // 用户密码
    private Handler handler; // 用于处理注册成功或失败的结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取布局中的View组件
        final ImageView accountView = findViewById(R.id.account_img2);
        final ImageView accountClose = findViewById(R.id.account_close2);
        final ImageView passwordView = findViewById(R.id.password_img2);
        final ImageView passwordClose = findViewById(R.id.password_close2);
        final ImageView passwordEye = findViewById(R.id.password_eye2);
        final ImageView surePasswordClose = findViewById(R.id.sure_password_close2);
        final ImageView surePasswordEye = findViewById(R.id.sure_password_eye2);

        // 设置输入框文本变化监听器
        EditText registerAccount = findViewById(R.id.register_account);
        registerAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                accountClose.setVisibility(View.GONE);
                accountView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                accountClose.setVisibility(View.VISIBLE);
                accountView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    accountClose.setVisibility(View.VISIBLE);
                    accountView.setVisibility(View.GONE);
                    account = editable.toString(); // 保存输入的账号
                } else {
                    accountClose.setVisibility(View.GONE);
                    accountView.setVisibility(View.VISIBLE);
                }
            }
        });

        EditText registerPassword = findViewById(R.id.register_password2);
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordClose.setVisibility(View.GONE);
                passwordView.setVisibility(View.VISIBLE);
                passwordEye.setVisibility(View.GONE);
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
                } else {
                    passwordClose.setVisibility(View.GONE);
                    passwordView.setVisibility(View.VISIBLE);
                    passwordEye.setVisibility(View.GONE);
                }
            }
        });

        EditText sureRegisterPassword = findViewById(R.id.sure_register_password2);
        sureRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surePasswordClose.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surePasswordClose.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    surePasswordClose.setVisibility(View.VISIBLE);
                    String password2 = registerPassword.getText().toString(); // 获取注册密码
                    if (password2.equals(editable.toString())) {
                        // 密码一致，显示对应的图标
                        surePasswordEye.setImageResource(R.drawable.sure); // 密码一致图标
                        password = password2;
                    } else {
                        // 密码不一致，显示对应的图标
                        surePasswordEye.setImageResource(R.drawable.close); // 密码不一致图标
                    }
                } else {
                    surePasswordClose.setVisibility(View.GONE);
                    passwordEye.setVisibility(View.GONE); // 隐藏图标
                }
            }
        });

        // 注册按钮点击事件
        findViewById(R.id.register2).setOnClickListener(view -> {

            MyRequest.register(account, password, new RegisterCallback() {
                @Override
                public void onSuccess(RegisterBean registerBean) {
                    // 处理注册成功的逻辑
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    // 例如，跳转到登录页面
                }

                @Override
                public void onFailure(Throwable throwable) {
                    // 处理注册失败的逻辑
                    Toast.makeText(getApplicationContext(), "注册失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        // 密码可见与隐藏的切换
        passwordEye.setOnClickListener(v -> {
            int inputType = registerPassword.getInputType();
            if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                passwordEye.setImageResource(R.drawable.closed_eye); // 设置为不可见图标
            } else {
                registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordEye.setImageResource(R.drawable.open_eye); // 设置为可见图标
            }
            // 光标保持在最后
            CharSequence charSequence = registerPassword.getText();
            if (charSequence != null) {
                Spannable spannable = (Spannable) charSequence;
                Selection.setSelection(spannable, charSequence.length());
            }
        });

        clearEditText(registerAccount, accountClose);
        clearEditText(registerPassword, passwordClose);
        clearEditText(sureRegisterPassword, surePasswordClose);

//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                MyResponse responseBody = (MyResponse) msg.obj;
//                if (responseBody.getCode() == Code.SUCCESS) {
//                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(intent); // 跳转到登录页面
//                    finish(); // 关闭当前页面
//                } else {
//                    Toast.makeText(RegisterActivity.this, responseBody.getMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
    }

    // 点击删除EditText内容功能
    private void clearEditText(EditText editText, ImageView closeView) {
        // 设置关闭按钮的点击事件
        closeView.setOnClickListener(v -> editText.setText("")); // 清空EditText内容
    }

    /**
     * 点击外部事件，关闭键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus(); // 获取当前聚焦的视图
            if (isShouldHideInput(v, ev)) { // 点击editText控件外部
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null; // 确保当前聚焦的视图不为空
                    KeyBoardUtil.closeKeyboard(v); // 关闭软键盘
                }
            }
            return super.dispatchTouchEvent(ev); // 继续分发触摸事件
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 判断是否需要隐藏输入法
     * @param v 当前聚焦的视图
     * @param event 触摸事件
     * @return 如果需要隐藏输入法，返回true；否则返回false
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) { // 如果当前视图是EditText
            int[] leftTop = {0, 0};
            // 获取输入框当前的位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 判断触摸事件是否在EditText范围内
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false; // 如果不是EditText，返回false
    }

}