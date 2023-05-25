package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    private ListView listView;
    private TextView playListName;
    private MusicAdapter musicAdapter;
    private List<Music> mySongList;
    private RelativeLayout relativeLayout;
    private ImageButton imageButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_song);

        dbHelper = DBHelper.getInstance(this);
        dbHelper.openWriteLink();
        dbHelper.openReadLink();

        // 初始化列表，绑定控件元素
        playListName = findViewById(R.id.song_list_name);
        imageButton = findViewById(R.id.startAllMusic);

        relativeLayout = findViewById(R.id.song_list_background);
        listView = findViewById(R.id.song_list);

        mySongList = new ArrayList<Music>();
        initSongList();
        musicAdapter = new MusicAdapter(SongListActivity.this, mySongList, null, null, null);


        listView.setAdapter(musicAdapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicService.MusicControl musicControl = MainActivity.musicControl;
                musicControl.refreshList(mySongList);
                musicControl.play();
                Toast.makeText(SongListActivity.this, "歌曲已全部加入播放队列中", Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    public void initSongList() {
        Intent intent = getIntent();
        int list_id = intent.getIntExtra("listId", 0);



        PlayList playList = dbHelper.findMusicListById(list_id);
        List<Music> musicList = playList.getMusicList();
        mySongList.clear();
        if (musicList == null) return;
        for (Music f : musicList) {
            mySongList.add(f);
        }
        relativeLayout.setBackgroundResource(playList.getListPicturePath());
        playListName.setText(playList.getListName());
    }

}
