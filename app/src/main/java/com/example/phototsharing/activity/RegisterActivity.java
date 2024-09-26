package com.example.phototsharing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.phototsharing.R;
import com.example.phototsharing.entity.ResponseBody;
import com.example.phototsharing.net.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;  // 用户名输入框
    private EditText passwordEditText;  // 密码输入框
    private Button registerButton;       // 注册按钮


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

        // 绑定视图组件
        usernameEditText = findViewById(R.id.register_account);
        passwordEditText = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register);

        // 设置按钮点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();  // 调用注册方法
            }
        });
    }

    private void registerUser() {
        // 获取用户输入
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // 创建请求体对象
        ApiInterface.RegisterRequest request = new ApiInterface.RegisterRequest(username, password);

        // 创建 API 服务实例
        ApiInterface apiService = RetrofitClient.getInstance().create(ApiInterface.class);

        // 发起注册请求
        Call<ResponseBody> call = apiService.registerUser(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 请求成功
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 网络错误处理
                Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}