package com.example.musicplayer.MenuFragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.MenuFragment.Adapter.DataBean;
import com.example.musicplayer.MenuFragment.Adapter.ImageAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.SongListActivity;
import com.google.android.material.snackbar.Snackbar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private Banner banner;

    public HomeFragment( Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.sing_list_1).setOnClickListener(this);

        banner = view.findViewById(R.id.banner);
        ImageAdapter imageAdapter = new ImageAdapter(DataBean.getTestData());
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
                intent.putExtra("platListId", 1);
                startActivity(intent);

        }
    }
}
