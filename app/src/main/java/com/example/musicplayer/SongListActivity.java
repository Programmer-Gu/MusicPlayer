package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity  extends AppCompatActivity {
    private ListView listView;
    private TextView playListName;
    private MusicAdapter musicAdapter;
    private List<Music> mySongList;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_song);

        // 初始化列表，绑定控件元素
        mySongList = new ArrayList<Music>();
        musicAdapter = new MusicAdapter(SongListActivity.this, mySongList);

        playListName = findViewById(R.id.song_list_name);
        relativeLayout = findViewById(R.id.song_list_background);
        listView = findViewById(R.id.song_list);
        listView.setAdapter(musicAdapter);

        initSongList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);
                
            }
        });
    }

    public void initSongList(){
        Intent intent = getIntent();
        List<Music> musicList = (List<Music>) intent.getSerializableExtra("musicList");
        mySongList.clear();
        if( musicList == null )return;
        for( Music f : musicList ){
            mySongList.add(f);
        }
        relativeLayout.setBackgroundResource(intent.getIntExtra("cover",0));
        playListName.setText(intent.getIntExtra("ListName",0));
    }

}
