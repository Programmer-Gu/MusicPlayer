package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private static ImageView songCover;
    private static int nowCover;
    private Intent intent;
    private MusicService.MusicControl musicControl;
    private ObjectAnimator animator;
    private ImageButton btn_play_pause, btn_next, btn_previous;
    //记录服务是否被解绑，默认没有

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        musicControl = MainActivity.musicControl;
        //获取从frag1传来的消息（音乐的选择列表）
        intent = getIntent();
        init();
    }

    private void init() {
        nowCover = -1145414;

        songCover = findViewById(R.id.song_cover);
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


        animator = ObjectAnimator.ofFloat(songCover, "rotation", 0f, 360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环

        if( musicControl.getMusicState() ){
            btn_play_pause.setImageResource(R.drawable.ic_stop);
            animator.pause();
        }
        else{
            btn_play_pause.setImageResource(R.drawable.ic_play);
            animator.start();
        }
    }

    public static Handler handler = new Handler() {
        //歌曲是多少分钟多少秒钟
        public String toTime( int duration ){
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
            return strMinute + ":" + strSecond;
        }
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();//获取从子线程发送过来的音乐播放进度
            //获取当前进度currentPosition和总时长duration
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            int cover = bundle.getInt("cover");
            //对进度条进行设置
            sb_progress.setMax(duration);
            sb_progress.setProgress(currentPosition);
            //文本设置
            tv_songName.setText(bundle.getString("songName"));
            //对封面进行设置
            if( cover != nowCover ){
                songCover.setImageResource(cover);
                nowCover = cover;
            }
            //显示当前歌曲已经播放的事件
            tv_progress.setText(toTime(currentPosition));
            tv_total.setText(toTime(duration));
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_pause:
                // 播放/暂停按钮点击事件
                if (musicControl.getMusicState()) {
                    //切换图标
                    musicControl.pausePlay();
                    btn_play_pause.setImageResource(R.drawable.ic_play);
                    animator.pause();
                } else {
                    btn_play_pause.setImageResource(R.drawable.ic_stop);
                    if( !musicControl.musicIsNull() ){
                        musicControl.continuePlay();
                    }
                    musicControl.play();
                    animator.start();
                }
                break;
            // TODO: 2023/5/22 点击播放音乐后，创建播放音乐列表，在里面进行上一首下一首的操作
            case R.id.btn_previous:
                musicControl.previousMusic();
            case R.id.btn_next:
                musicControl.nextMusic();
                break;
        }
    }
}