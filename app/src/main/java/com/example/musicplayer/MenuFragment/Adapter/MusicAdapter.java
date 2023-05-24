package com.example.musicplayer.MenuFragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicplayer.R;
import com.example.musicplayer.entity.Music;

import java.util.List;
public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> list;

    public MusicAdapter(Context context, List<Music> musicList) {
        super();
        this.context = context;
        this.list = musicList;
    }

    @Override
    public int getCount() {
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

        // 获取当前位置的书籍信息
        Music music = list.get(position);
        // 获取布局文件中的控件对象
        TextView singName = view.findViewById(R.id.music_name);
        TextView singerName = view.findViewById(R.id.singer_name);
        ImageView singIcon = view.findViewById(R.id.music_icon);

        // 设置控件显示的书籍信息
        singName.setText(music.getMusicName());
        singerName.setText(music.getSingerName());
        singIcon.setImageResource(music.getCoverPath());
        return view;
    }
}
