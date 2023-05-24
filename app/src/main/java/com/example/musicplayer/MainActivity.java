package com.example.musicplayer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.musicplayer.DBHelper.DBHelper;
import com.example.musicplayer.MenuFragment.CategoryFragment;
import com.example.musicplayer.MenuFragment.HomeFragment;
import com.example.musicplayer.MenuFragment.PersonalFragment;
import com.example.musicplayer.Service.MusicService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static MusicService.MusicControl musicControl;
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //声明三个Tab的布局文件
    private LinearLayout mTab1, mTab2, mTab3;
    //声明三个Tab的ImageButton
    private ImageButton mImg1, mImg2, mImg3, toStartMusic, musicPlayer;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private ServiceConnection conn;
    private Intent serviceIntent;
    private boolean isUnbind;

    @Override
    protected void onStart() {
        super.onStart();
        //建立数据库对象
        dbHelper = DBHelper.getInstance(this);
        //打开数据库读写连接
        dbHelper.openReadLink();
        dbHelper.openWriteLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
        initService();//初始化服务

        sharedPreferences = getSharedPreferences("root", Context.MODE_PRIVATE);
        //查询登录状态，如果没有登录，跳转到登录界面
        boolean login_status = sharedPreferences.getBoolean("login_status", false);
        if (!login_status) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initEvents() {
        //初始化四个Tab的点击事件
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        toStartMusic.setOnClickListener(this);
        musicPlayer.setOnClickListener(this);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        //初始化四个Tab的布局文件
        mTab1 = (LinearLayout) findViewById(R.id.id_tab1);
        mTab2 = (LinearLayout) findViewById(R.id.id_tab2);
        mTab3 = (LinearLayout) findViewById(R.id.id_tab3);

        //初始化四个ImageButton
        mImg1 = (ImageButton) findViewById(R.id.id_tab_img1);
        mImg2 = (ImageButton) findViewById(R.id.id_tab_img2);
        mImg3 = (ImageButton) findViewById(R.id.id_tab_img3);

        toStartMusic = findViewById(R.id.music_image);
        musicPlayer = findViewById(R.id.button_player);
    }

    private void initDatas() {
        isUnbind = false;

        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new HomeFragment(MainActivity.this));
        mFragments.add(new CategoryFragment());
        mFragments.add(new PersonalFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }
        };
        //设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void initService(){
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                musicControl = (MusicService.MusicControl) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("gzc", "服务连接失败");
            }
        };//创建服务连接对象
        serviceIntent = new Intent(MainActivity.this,MusicService.class);
        bindService( serviceIntent,conn,Context.BIND_AUTO_CREATE);//绑定服务
    }

    //处理Tab的点击事件
    @Override
    public void onClick(View v) {
        resetImgs(); //先将四个ImageButton置为灰色
        switch (v.getId()) {
            case R.id.id_tab1:
                selectTab(0);
                break;
            case R.id.id_tab2:
                selectTab(1);
                break;
            case R.id.id_tab3:
                selectTab(2);
                break;
            case R.id.button_player:
                if( musicControl.getMusicState() ){
                    musicPlayer.setImageResource(R.drawable.ic_play);
                    musicControl.pausePlay();
                }
                else{
                    musicPlayer.setImageResource(R.drawable.ic_stop);
                    if( !musicControl.musicIsNull() ){
                        musicControl.continuePlay();
                    }
                    musicControl.play();
                }
                break;
            case R.id.music_image:
                Intent intent = new Intent( MainActivity.this, MusicPlayerActivity.class );
                startActivity(intent);
                break;
        }
    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImg1.setImageResource(R.drawable.icon);
                break;
            case 1:
                mImg2.setImageResource(R.drawable.music_list_1);
                break;
            case 2:
                mImg3.setImageResource(R.drawable.my_info_1);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将三个ImageButton置为灰色
    private void resetImgs() {
        mImg1.setImageResource(R.drawable.icon_2);
        mImg2.setImageResource(R.drawable.music_list_2);
        mImg3.setImageResource(R.drawable.my_info_2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isUnbind) {
            musicControl.pausePlay();
            unbindService(conn);
        }
    }
}
