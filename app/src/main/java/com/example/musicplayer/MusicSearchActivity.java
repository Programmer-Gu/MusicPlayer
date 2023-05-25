package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class MusicSearchActivity extends AppCompatActivity {
    private ListView listView;
    private EditText editText;
    private Button startSearch;
    private DBHelper dbHelper;
    private List<PlayList> playList_data;
    private List<Integer> play_list;
    private SharedPreferences sharedPreferences;
    private List<Music> allMusic;
    private MusicAdapter musicAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_music);

        listView = findViewById(R.id.music_list);
        startSearch = findViewById(R.id.beginSearch);
        editText = findViewById(R.id.searchText);

        sharedPreferences = getSharedPreferences("root", Context.MODE_PRIVATE);
        //建立数据库对象
        dbHelper = DBHelper.getInstance(this);
        //打开数据库读写连接
        dbHelper.openReadLink();

        playList_data = new ArrayList<>();
        int user_id = sharedPreferences.getInt("user_id", -114514 );
        if( user_id == -114514 )return;
        play_list = dbHelper.getPlaylistByUserId(user_id);
        for( int f : play_list ){
            playList_data.add(dbHelper.findMusicListById(f));
        }

        allMusic = dbHelper.getAllMusic();
        musicAdapter = new MusicAdapter(MusicSearchActivity.this,allMusic,playList_data,play_list,dbHelper);
        listView.setAdapter(musicAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);
                MusicService.MusicControl musicControl = MainActivity.musicControl;
                musicControl.addNextMusic(allMusic.get(position));
                musicControl.nextMusic();
                Intent intent = new Intent(MusicSearchActivity.this, PlayList.class);
                startActivity(intent);
            }
        });

        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = editText.getText().toString();
                List<Music> getResult = dbHelper.searchMusicByName(searchText);
                if( getResult.size() > 0 ){
                    allMusic.clear();
                    for( Music f : getResult ){
                        allMusic.add(f);
                    }
                    musicAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MusicSearchActivity.this,"没有符合标题的检索结果哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}