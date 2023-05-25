package com.example.musicplayer.MenuFragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.entity.PlayList;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private List<PlayList> playLists;
    private Context context;

    public MusicListAdapter(Context context, List<PlayList> playLists ){
        this.playLists = playLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(playLists == null )return 0;
        return playLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_layout, viewGroup, false);
        }

        ImageView imageView = view.findViewById(R.id.image_cover);
        TextView textView = view.findViewById(R.id.text_title);

        imageView.setImageResource(playLists.get(position).getListPicturePath());
        textView.setText(playLists.get(position).getListName());
        return view;
    }
}
