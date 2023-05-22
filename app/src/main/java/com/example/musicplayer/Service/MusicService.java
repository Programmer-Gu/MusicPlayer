package com.example.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;


import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private MediaPlayer player;   //声明一个MediaPlayer引用
    private Timer timer; //声明一个计时器引用

    /**
     * 无参构造方法
     */
    private MusicService() {
    }


    @Override
    public IBinder onBind(Intent intent) {

        return new MusicControl();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建音乐播放器对象
        player = new MediaPlayer();
    }

    /**
     * //添加计时器用于设置音乐播放器中的播放进度条
     */
    public void addTimer() {
        if (timer == null) {
            //创建计时器对象
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (player == null) return;
                    int duration = player.getDuration();//获取歌曲总时长
                    int currentPosition = player.getCurrentPosition();//获取播放进度

                    //todo 先写MusicPlayerActivity
//                    Message msg= MusicPlayerActivity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至bundle中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    //再将bundle封装到msg消息对象中

                    //最后将消息发送到主线程的消息队列中
                }
            };
            timer.schedule(timerTask, 5,500);
        }
    }


    /**
     * 内部类：跨进程通信音乐信息的辅助类
     */
   public class MusicControl extends Binder {
        public void play(int i) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + "music" + i);
            try {
                //重置音乐播放器
                player.reset();
                //加载多媒体文件
                player = MediaPlayer.create(getApplicationContext(), uri);
                //播放音乐
                player.start();
                addTimer();//添加计时器
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //调用MediaPlayer自带的方法实现音乐的暂停、继续和退出

        /**
         * 音乐的暂停
         */
        public void pausePlay() {
            player.pause();
        }

        /**
         * 继续播放音乐
         */
        public void continuePlay() {
            player.start();
        }

        /**
         * 设置音乐的播放位置
         */
        public void seekTo(int progress) {
            player.seekTo(progress);
        }
    }

    //销毁多媒体播放器
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player == null) return;
        if (player.isPlaying()) player.stop();//停止播放音乐
        player.release();//释放占用的资源
        player = null; //player置为空
    }
}
