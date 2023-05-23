package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.musicplayer.Service.MusicService;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    //进度条
    private static SeekBar seekBar;
    private static TextView tv_progress, tv_total, tv_songName;
    private static SeekBar sb_progress;
    private Intent intent1;
    private boolean isPlaying = false;
    private MusicService.MusicControl musicControl;
    private ObjectAnimator animator;
    private MyServiceConn conn;
    private ImageButton btn_play_pause;
    private ImageButton btn_next;
    private ImageButton btn_previous;
    //记录服务是否被解绑，默认没有
    private boolean isUnbind = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //获取从frag1传来的消息（音乐的选择列表）
        Intent intent1 = getIntent();
        init();
    }

    //    public static Handler handler=new Handler(){
//        @SuppressLint("HandlerLeak")
//        @Override
//        public  void handlerMessage(Message msg){
//            Bundle bundle=msg.getData();//获取子线程发送过来的音乐播放进度
//            //获取当前进度currentPosition和总市场duration
//            int duration=bundle.getInt("duration");
//            int currentPosition=bundle.getInt("currentPosition");
//
//
//        }
//    };
    private void init() {
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        sb_progress = findViewById(R.id.sb_progress);
        //歌曲名显示
        tv_songName = findViewById(R.id.tv_songName);
        //按钮点击事件
        btn_play_pause = findViewById(R.id.btn_play_pause);
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        btn_play_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //仅当滑动条到末端时，结束动画
                if (progress == seekBar.getMax()) {
                    animator.pause();//停止播放动画
                }
            }

            @Override
            //滑动条开始滑动时调用
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            //滑动条停止滑动时调用
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();//获取进度条的进度
                musicControl.seekTo(progress);
            }
        });
        //声明并绑定音乐播放器iv_music控件
        ImageView iv_music = findViewById(R.id.iv_music);
        String position = intent1.getStringExtra("position");

        int i = Integer.parseInt(position);
//        iv_music.setImageResource(frag1.icons[i]);//这里获取音乐的封面
        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();//获取从子线程发送过来的音乐播放进度
            //获取当前进度currentPosition和总时长duration
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            //对进度条进行设置
            sb_progress.setMax(duration);
            sb_progress.setProgress(currentPosition);
            //歌曲是多少分钟多少秒钟
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            //对分钟和秒进行字符串处理
            String strMinute = String.valueOf(minute);
            String strSecond = String.valueOf(second);
            if (minute < 10) {//如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            }
            if (second < 10) {//如果歌曲中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            }
            //显示当前歌曲已经播放的事件
            tv_progress.setText(strMinute + ":" + strSecond);
        }
    };

    //用于实现连接服务
    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void unbind(boolean isUnbind) {
        //如果解绑了
        if (isUnbind) {
            musicControl.pausePlay();
            unbindService(conn);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_pause:
                // 播放/暂停按钮点击事件
                if (isPlaying) {
                    //切换图标
                    btn_play_pause.setImageResource(R.drawable.ic_stop);

                    String position = intent1.getStringExtra("position");
                    int i = Integer.parseInt(position);
                    musicControl.play(i);
                    animator.start();
                } else {
                    musicControl.pausePlay();
                    animator.pause();
                }
                //切换播放状态
                isPlaying = !isPlaying;
                break;

            // TODO: 2023/5/22 点击播放音乐后，创建播放音乐列表，在里面进行上一首下一首的操作
            case R.id.btn_previous:
            case R.id.btn_next:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind);
    }
}