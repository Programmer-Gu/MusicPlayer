package com.example.musicplayer.MenuFragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.LogHelper.DBLog;
import com.example.musicplayer.LoginActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private TextView tv_nickname;
    private TextView tv_email;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;
    private Intent intent;
    private ImageView iv_head;
    private AlertDialog dialog;
    private EditText etNewNickname;
    private Button btnConfirm;


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

        //更换头像点击imageview事件
        iv_head = view.findViewById(R.id.iv_head);
        iv_head.setOnClickListener(this);

        //检查登录状态并且渲染用户信息
        boolean login_status = sharedPreferences.getBoolean("login_status", false);
        if (!login_status) {
            DBHelper.showToast(getContext(), "数据有误，请退出登录后再次登录！");
        } else {
            int user_id = sharedPreferences.getInt("user_id", 0);
            if (user_id == 0) {
                Log.d("sharedPreferences操作错误", "没有获取到用户的id");
            } else {
                //数据库查询并设置用户昵称和邮箱，并设置头像
                User user = dbHelper.getUserById(user_id);
                tv_nickname.setText(user.getNickname());
                tv_email.setText(user.getUser_email());
                iv_head.setImageResource(user.getHeadPicturePath());
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
            case R.id.iv_head:

                List<Integer> avatarList = new ArrayList<>();
                avatarList.add(R.drawable.bhead1);
                avatarList.add(R.drawable.bhead2);
                avatarList.add(R.drawable.bhead3);
                avatarList.add(R.drawable.bhead4);
                avatarList.add(R.drawable.ghead1);
                avatarList.add(R.drawable.ghead2);
                avatarList.add(R.drawable.ghead3);
                avatarList.add(R.drawable.ghead4);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择头像");


                // 创建适配器
                AvatarAdapter adapter = new AvatarAdapter(getContext(), avatarList);

                // 设置列表视图的适配器
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();

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

        etNewNickname = dialogView.findViewById(R.id.et_new_nickname);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
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

    // 自定义适配器
    class AvatarAdapter extends ArrayAdapter<Integer> {
        private Context context;
        private List<Integer> avatarList;

        public AvatarAdapter(Context context, List<Integer> avatarList) {
            super(context, 0, avatarList);
            this.context = context;
            this.avatarList = avatarList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_avatar, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.imageViewAvatar);

            // 设置头像图片
            int avatarResId = avatarList.get(position);
            imageView.setImageResource(avatarResId);

            // 设置点击监听器
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = sharedPreferences.getInt("user_id", 0);
                    if (user_id == 0) {
                        DBHelper.showToast(getContext(), "您的登录已失效，请重新登录~");
                        dialog.dismiss();
                        return;
                    }
                    // 执行设置头像的操作，例如更新数据模型或调用网络请求
                    iv_head.setImageResource(avatarResId);

                    dbHelper.updateHeadPicturePathById(user_id, avatarResId);
                    // 显示设置成功的提示
                    Toast.makeText(getContext(), "头像设置成功", Toast.LENGTH_SHORT).show();

                    // 关闭对话框
                    dialog.dismiss();
                }
            });

            return convertView;
        }
    }


}