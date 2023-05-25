package com.example.musicplayer.MenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.MenuFragment.Adapter.MusicListAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements View.OnClickListener {
    private List<PlayList> playList_data;
    private ListView listView;
    private MusicListAdapter musicListAdapter;
    private Context context;
    private List<Integer> play_list;
    private MainActivity aMain;

    private DBHelper dbHelper;
    private Button btnConfirm;
    private Button btnCancel;
    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;
    private ImageView iv_picture;

    private Integer picturePath=R.drawable.cover1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建数据库管理对象
        dbHelper = DBHelper.getInstance(getContext());
        //打开数据库读写连接
        dbHelper.openReadLink();
        dbHelper.openWriteLink();
        sharedPreferences = context.getSharedPreferences("root", Context.MODE_PRIVATE);
    }

    public CategoryFragment(Context context, List<PlayList> playList_data, List<Integer> play_list) {
        // Required empty public constructor
        this.playList_data = playList_data;
        this.context = context;
        this.play_list = play_list;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // 在这里进行你的Fragment布局的初始化和相关逻辑的处理
        listView = view.findViewById(R.id.all_song_list);
        view.findViewById(R.id.add_list).setOnClickListener(this);
        musicListAdapter = new MusicListAdapter(context, playList_data);
        listView.setAdapter(musicListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);

                //开启歌单跳转页面
                Intent intent = new Intent(getContext(), SongListActivity.class);
                if (playList_data.size() > position) {
                    intent.putExtra("listId", play_list.get(position));
                }
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_list:
                SharedPreferences sharedPreferences = context.getSharedPreferences("root", Context.MODE_PRIVATE);
                int user_id = sharedPreferences.getInt("user_id", 0);
                if (user_id == 0) {
                    DBHelper.showToast(getContext(), "您的登录已失效，请重新登录~");
                    return;
                }
                showAddListDialog();
                break;
        }
    }

    private void showAddListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_playlist, null);
        dialogBuilder.setView(dialogView);

        EditText et_ListName = dialogView.findViewById(R.id.et_ListName);
        iv_picture = dialogView.findViewById(R.id.iv_picture);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        //选择封面监听
        iv_picture.setOnClickListener(view -> {
            List<Integer> pictureList = new ArrayList<>();
            pictureList.add(R.drawable.cover1);
            pictureList.add(R.drawable.cover2);
            pictureList.add(R.drawable.cover3);
            pictureList.add(R.drawable.cover4);
            pictureList.add(R.drawable.cover5);
            pictureList.add(R.drawable.cover6);


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("选择封面");


            // 创建适配器
            PictureAdapter adapter = new PictureAdapter(getContext(), pictureList);

            // 设置列表视图的适配器
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // 设置确定按钮的点击事件
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            private String listName;

            @Override
            public void onClick(View v) {
                //修改数据库内容
                int user_id = sharedPreferences.getInt("user_id", 0);
                if (user_id == 0) {
                    DBHelper.showToast(getContext(), "您的登录已失效，请重新登录~");
                    return;
                }


                //修改页面显示
                listName = et_ListName.getText().toString();

                if (listName.isEmpty()) {
                    Toast.makeText(getContext(), "请输入歌单名称喔~", Toast.LENGTH_SHORT).show();
                    return;
                }

                long playListId = dbHelper.insertPlaylist(listName, picturePath, user_id);
                dbHelper.insertUserPlaylist(user_id, playListId);
                DBHelper.showToast(getContext(), "歌单添加成功");

                refreshData(user_id);
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

    public void refreshData(int user_id) {
        List<Integer> play_list = dbHelper.getPlaylistByUserId(user_id);
        playList_data.clear();
        for (int f : play_list) {
            playList_data.add(dbHelper.findMusicListById(f));
        }
        musicListAdapter.notifyDataSetChanged();
    }

    class PictureAdapter extends ArrayAdapter<Integer> {
        private Context context;
        private List<Integer> PictureList;

        public PictureAdapter(Context context, List<Integer> PictureList) {
            super(context, 0, PictureList);
            this.context = context;
            this.PictureList = PictureList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_avatar, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.imageViewAvatar);

            // 设置封面图片
            int pictureId = PictureList.get(position);
            imageView.setImageResource(pictureId);

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

                    // 执行设置封面的操作，更新数据

                    iv_picture.setImageResource(pictureId);
                    Log.d("pictureId", "" + pictureId);

                    // 显示设置成功的提示
                    Toast.makeText(getContext(), "封面设置成功", Toast.LENGTH_SHORT).show();

                    // 关闭对话框
                    dialog.dismiss();
                    picturePath = pictureId;
                }
            });

            return convertView;
        }
    }
}