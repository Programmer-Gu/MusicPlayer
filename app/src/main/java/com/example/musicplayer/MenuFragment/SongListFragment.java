package com.example.musicplayer.MenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {
    private Context context;
    private ListView listView;
    private MusicAdapter musicAdapter;
    private List<Music> mySongList;
    private RelativeLayout relativeLayout;

    public SongListFragment(Context context){
        this.context = context;
        mySongList = new ArrayList<Music>();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);

        musicAdapter = new MusicAdapter(this.context, mySongList);
        listView = view.findViewById(R.id.song_list);
        listView.setAdapter(musicAdapter);
        relativeLayout = view.findViewById(R.id.song_list_background);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View itemView = listView.getChildAt(position);
                
            }
        });
        return view;
    }

    public void refreshSongList( int songListBackground, List<Music> songList ){
        mySongList.clear();
        for( Music f : songList ){
            mySongList.add(f);
        }
        relativeLayout.setBackgroundResource(songListBackground);
        musicAdapter.notifyDataSetChanged();
    }
}
