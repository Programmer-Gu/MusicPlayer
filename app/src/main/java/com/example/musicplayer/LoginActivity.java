package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.LogHelper.DBLog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email;
    private EditText et_password;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private CheckBox ck_privacy;
    private boolean agree = false;//用户是否同意隐私协议

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
        sharedPreferences = getSharedPreferences("root", Context.MODE_PRIVATE);

        //隐私内容
        findViewById(R.id.tv_privacy_policy).setOnClickListener(this);
        ck_privacy = findViewById(R.id.ck_privacy);
        ck_privacy.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                agree = true;
            } else {
                agree = false;
            }
        });


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
                if (!agree) {
                    Toast.makeText(this, "请勾选用户隐私协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                int user_id = dbHelper.loginUser(this, email, password);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (user_id == -1) {
                    Log.d("用户登录:", "登录失败");
                    editor.putBoolean("login_status", false);
                    editor.apply();
                } else {
                    Log.d("用户登录:", "登录成功");
                    //登录成功后，获取用户id放入sharedPreferences
                    editor.putInt("user_id", user_id);
                    editor.putBoolean("login_status", true);
                    editor.apply();
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();

                break;
            case R.id.btn_register:
                intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_privacy_policy:
                showPrivacyPolicyDialog(getResources().getString(R.string.privacy_policy));
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }


    private void showPrivacyPolicyDialog(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("用户隐私协议");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮的操作，可以留空或添加特定的逻辑
                dialog.dismiss();
            }
        });
        dialogBuilder.create().show();
    }
}