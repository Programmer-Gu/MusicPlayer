package com.example.musicplayer.MenuFragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.User;

public class PersonalFragment extends Fragment {

    private TextView tv_nickname;
    private TextView tv_email;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper ;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        // 在这里进行你的Fragment布局的初始化和相关逻辑的处理

        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_email = view.findViewById(R.id.tv_email);

        //获取sharedPreferences对象
        sharedPreferences = getContext().getSharedPreferences("root", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);
        if (user_id == 0) {
            Log.d("sharedPreferences操作错误", "没有获取到用户的id");
        } else {
            //数据库查询并设置用户昵称和邮箱
            User user = dbHelper.getUserById(user_id);
            tv_nickname.setText(user.getNickname());
            tv_email.setText(user.getUser_email());
        }

        return view;
    }


}