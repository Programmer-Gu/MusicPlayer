package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.LogHelper.DBLog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email;
    private EditText et_password;
    private DBHelper dbHelper;

    @Override
    protected void onStart() {
        super.onStart();
        //建立数据库对象
        dbHelper = DBHelper.getInstance(this);
        //打开数据库读写连接
        dbHelper.openReadLink();
        dbHelper.openWriteLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                // 检查输入是否不完整
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean b = dbHelper.loginUser(this, email, password);
                if (b) {
                    Log.d("用户登录:", "登录成功");
                } else {
                    Log.d("用户登录:", "登录失败");
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}