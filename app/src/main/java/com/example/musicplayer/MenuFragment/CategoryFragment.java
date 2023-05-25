package com.example.musicplayer.MenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.MainActivity;
import com.example.musicplayer.MenuFragment.Adapter.MusicListAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private List<PlayList> playList_data;
    private ListView listView;
    private MusicListAdapter musicListAdapter;
    private Context context;
    private List<Integer> play_list;
    private MainActivity aMain;
    public CategoryFragment(Context context, List<PlayList> playList_data, List<Integer> play_list, MainActivity aMain) {
        // Required empty public constructor
        this.playList_data = playList_data;
        this.context = context;
        this.play_list = play_list;
        this.aMain = aMain;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // 在这里进行你的Fragment布局的初始化和相关逻辑的处理
        listView = view.findViewById(R.id.all_song_list);

        musicListAdapter = new MusicListAdapter(context,playList_data);
        listView.setAdapter( musicListAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);

                //开启歌单跳转页面
                Intent intent = new Intent( context, SongListActivity.class );
                if( playList_data.size() > position ){
                    PlayList playList = playList_data.get(position);
                    intent.putExtra("musicList", new ArrayList<>(playList.getMusicList()));
                    intent.putExtra("cover", playList.getListPicturePath());
                    intent.putExtra("ListName", playList.getListName());
                }
                startActivity(intent);
            }
        });
        return view;
    }
}