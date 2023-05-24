package com.example.musicplayer.MenuFragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.LogHelper.DBLog;
import com.example.musicplayer.LoginActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.User;

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private TextView tv_nickname;
    private TextView tv_email;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;
    private Intent intent;


    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建数据库管理对象
        dbHelper = DBHelper.getInstance(getContext());
        //打开数据库读写连接
        dbHelper.openReadLink();
        dbHelper.openWriteLink();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        // 在这里进行你的Fragment布局的初始化和相关逻辑的处理

        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_email = view.findViewById(R.id.tv_email);
        view.findViewById(R.id.btn_edit_nickname).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);

        //获取sharedPreferences对象
        sharedPreferences = getContext().getSharedPreferences("root", Context.MODE_PRIVATE);

        //检查登录状态并且渲染用户信息
        boolean login_status = sharedPreferences.getBoolean("login_status", false);
        if (!login_status) {
            DBHelper.showToast(getContext(), "数据有误，请退出登录后再次登录！");
        } else {
            int user_id = sharedPreferences.getInt("user_id", 0);
            if (user_id == 0) {
                Log.d("sharedPreferences操作错误", "没有获取到用户的id");
            } else {
                //数据库查询并设置用户昵称和邮箱
                User user = dbHelper.getUserById(user_id);
                tv_nickname.setText(user.getNickname());
                tv_email.setText(user.getUser_email());
            }
        }

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_nickname:
                showEditNicknameDialog();
                break;
            case R.id.btn_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", 0);
                editor.putBoolean("login_status", false);
                editor.apply();

                intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

                break;
        }
    }


    /**
     * 用于显示用户隐私协议
     */
    private void showEditNicknameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_nickname, null);
        dialogBuilder.setView(dialogView);

        EditText etNewNickname = dialogView.findViewById(R.id.et_new_nickname);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);


        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // 设置确定按钮的点击事件
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改数据库内容
                int user_id = sharedPreferences.getInt("user_id", 0);
                if (user_id == 0) {
                    DBHelper.showToast(getContext(), "您的登录已失效，请重新登录~");
                    return;
                }
                //修改页面显示
                String newNickname = etNewNickname.getText().toString();
                dbHelper.updateNicknameById(user_id, newNickname);
                DBHelper.showToast(getContext(), "昵称修改成功");
                tv_nickname.setText(newNickname);
                dialog.dismiss();

            }
        });

        // 设置取消按钮的点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}