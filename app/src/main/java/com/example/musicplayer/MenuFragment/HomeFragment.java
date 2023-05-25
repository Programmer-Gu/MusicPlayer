package com.example.musicplayer.MenuFragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.MenuFragment.Adapter.DataBean;
import com.example.musicplayer.MenuFragment.Adapter.ImageAdapter;
import com.example.musicplayer.MenuFragment.Adapter.MusicAdapter;
import com.example.musicplayer.MenuFragment.Adapter.MusicListAdapter;
import com.example.musicplayer.MusicSearchActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;
import com.google.android.material.snackbar.Snackbar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private Banner banner;
    private List<PlayList> playList_data;
    private List<Music> hotMusic;
    private List<Integer> play_list;
    private DBHelper dbHelper;

    public HomeFragment(Context context, DBHelper dbHelper, List<PlayList> playList_data, List<Integer> play_list) {
        this.playList_data = playList_data;
        this.dbHelper = dbHelper;
        this.play_list = play_list;
        this.context = context;
        hotMusic = new ArrayList<>();
        insertElement();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.sing_list_1).setOnClickListener(this);
        view.findViewById(R.id.jump_to_search).setOnClickListener(this);

        banner = view.findViewById(R.id.banner);
        ImageAdapter imageAdapter = new ImageAdapter(DataBean.getTestData());

        MusicAdapter musicAdapter = new MusicAdapter(context, hotMusic, playList_data, play_list, dbHelper);
        ListView hotListView = view.findViewById(R.id.hotMusic);
        hotListView.setAdapter(musicAdapter);

        //加载本地图片
        banner.setAdapter(imageAdapter)
                .addBannerLifecycleObserver((LifecycleOwner) context)
                .setIndicator(new CircleIndicator(context))
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {
                        Snackbar.make(banner, ((DataBean) data).title, Snackbar.LENGTH_SHORT).show();
                        Log.i(TAG, "position: " + position);
                    }
                });
        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sing_list_1:
                //设置当前点击的Tab所对应的页面
                Intent intent = new Intent( context, SongListActivity.class );
                if( playList_data == null )return;
                if( playList_data.size() > 0 ){
                    PlayList playList = playList_data.get(0);
                    intent.putExtra("musicList", new ArrayList<>(playList.getMusicList()));
                    intent.putExtra("cover", playList.getListPicturePath());
                    intent.putExtra("ListName", playList.getListName());
                }
                startActivity(intent);
            case R.id.jump_to_search:
                Intent myIntent = new Intent(context, MusicSearchActivity.class);
                startActivity(myIntent);
        }
    }

    public void insertElement(){
        Music music1 = new Music();
        music1.setMusicName("起风了");
        music1.setMusicPath(R.raw.qifengle);
        music1.setCoverPath(R.drawable.qifengle);
        music1.setSingerName("卖辣椒也用券");
        hotMusic.add(music1);
        Music music2 = new Music();
        music2.setMusicName("屋顶");
        music2.setMusicPath(R.raw.wuding);
        music2.setCoverPath(R.drawable.wuding);
        music2.setSingerName("周杰伦");
        hotMusic.add(music2);
        Music music3 = new Music();
        music3.setMusicName("稻香");
        music3.setMusicPath(R.raw.daoxiang);
        music3.setCoverPath(R.drawable.daoxiang);
        music3.setSingerName("周杰伦");
        hotMusic.add(music3);
        Music music4 = new Music();
        music4.setMusicName("大约在冬季");
        music4.setMusicPath(R.raw.dayuezaidongji);
        music4.setCoverPath(R.drawable.dayuezaidongji);
        music4.setSingerName("齐秦");
        hotMusic.add(music4);
        Music music5 = new Music();
        music5.setMusicName("平凡之路");
        music5.setMusicPath(R.raw.pingfanzhilu);
        music5.setCoverPath(R.drawable.pingfanzhilu);
        music5.setSingerName("朴树");
        hotMusic.add(music5);
        Music music6 = new Music();
        music6.setMusicName("玫瑰花的葬礼");
        music6.setMusicPath(R.raw.meiguihuadezangli);
        music6.setCoverPath(R.drawable.meiguihuadezangli);
        music6.setSingerName("许嵩");
    }
}
