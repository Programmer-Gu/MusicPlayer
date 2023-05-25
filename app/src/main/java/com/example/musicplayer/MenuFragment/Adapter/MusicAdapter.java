package com.example.musicplayer.MenuFragment.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;

import java.util.List;
public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> list;
    private List<PlayList> playList_data;
    private List<Integer> listNumbering;
    private DBHelper dbHelper;

    public MusicAdapter(Context context, List<Music> musicList, List<PlayList> playList_data,
                        List<Integer>listNumbering, DBHelper dbHelper) {
        super();
        this.listNumbering = listNumbering;
        this.playList_data = playList_data;
        this.dbHelper = dbHelper;
        this.context = context;
        this.list = musicList;
    }

    @Override
    public int getCount() {
        if( list == null )return 0;
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_layout, parent, false);
        }

        // 获取当前位置的乐曲信息
        Music music = list.get(position);
        // 获取布局文件中的控件对象
        TextView singName = view.findViewById(R.id.music_name);
        TextView singerName = view.findViewById(R.id.singer_name);
        ImageView singIcon = view.findViewById(R.id.music_icon);

        // 设置控件显示的乐曲信息
        singName.setText(music.getMusicName());
        singerName.setText(music.getSingerName());
        singIcon.setImageResource(music.getCoverPath());

        ImageButton nextButton = view.findViewById(R.id.to_play);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicService.MusicControl musicControl = MainActivity.musicControl;
                musicControl.addNextMusic(list.get(position));
                Toast.makeText(context, "已添加至下一首播放", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton addList = view.findViewById(R.id.heart);

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( dbHelper == null ){
                    Toast.makeText(context,"你现已经在歌单内部哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( playList_data == null || listNumbering == null ){
                    Toast.makeText(context,"你现在没有歌单哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateOperate( list.get(position));
            }
        });
        return view;
    }

    @SuppressLint("MissingInflatedId")
    private void updateOperate( Music music){
        // 创建一个对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 获取自定义对话框布局视图
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_song_list, null);

        // 将布局视图设置为对话框的视图
        builder.setView(view);

        // 获取对话框的按钮
        ListView listView = view.findViewById(R.id.select_list);

        MusicListAdapter musicListAdapter = new MusicListAdapter(context,playList_data);
        listView.setAdapter( musicListAdapter );
        AlertDialog dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);

                int music_id = dbHelper.getMusicIdByPath(music.getMusicPath());
                int list_id = listNumbering.get(position);
                dbHelper.insertPlaylistSong(list_id,music_id);
                Toast.makeText(context,"已将歌曲添加至歌单！", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
