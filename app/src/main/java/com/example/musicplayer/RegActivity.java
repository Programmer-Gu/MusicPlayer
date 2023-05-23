package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.FocusFinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.DBHelper.DBHelper;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email;
    private EditText et_password;
    private EditText et_confirmPassword;
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
        setContentView(R.layout.activity_reg);
        init();
    }

    private void init() {


        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirmPassword.getText().toString();

                //检验输入的完整性
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检验两个密码是否一致
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(this, "输入密码和重复密码不同", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean result = dbHelper.registerUser(email, password);
                if (result) {
                    Toast.makeText(this, "注册成功，赶快去登录叭！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "您的邮箱已经注册过啦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back:
                //返回按钮点击事件
                finish();
                break;
        }
    }
}