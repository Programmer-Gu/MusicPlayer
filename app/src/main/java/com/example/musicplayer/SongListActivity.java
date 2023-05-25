package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.Service.MusicService;
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
                MusicService.MusicControl musicControl = MainActivity.musicControl;
                musicControl.addNextMusic(mySongList.get(position));
                musicControl.nextMusic();
                Intent intent = new Intent(SongListActivity.this, PlayList.class);
                startActivity(intent);
            }
        });

        for( int i = 0; i < mySongList.size(); i++ ){
            View itemView = listView.getChildAt(i);
            Button nextButton = itemView.findViewById(R.id.to_play);
            int finalI = i;
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicService.MusicControl musicControl = MainActivity.musicControl;
                    musicControl.addNextMusic(mySongList.get(finalI));
                    Toast.makeText(SongListActivity.this, "已添加至下一首播放", Toast.LENGTH_SHORT).show();
                }
            });


        }
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
