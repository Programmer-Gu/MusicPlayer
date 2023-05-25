package com.example.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.example.musicplayer.MusicPlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.Music;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private MediaPlayer player;   //声明一个MediaPlayer引用
    private Timer timer; //声明一个计时器引用
    private LinkedList<Music> musicList;
    private ListIterator<Music> listIterator;
    private Thread songThread;
    private Music nowMusic;

    /**
     * 无参构造方法
     */
    public MusicService() {
        musicList = new LinkedList<>();

        Music music1 = new Music();
        music1.setMusicName("起风了");
        music1.setMusicPath(R.raw.qifengle);
        music1.setCoverPath(R.drawable.btn_circle);
        music1.setSingerName("卖辣椒也用券");
        musicList.add(music1);
        Music music2 = new Music();
        music2.setMusicName("屋顶");
        music2.setMusicPath(R.raw.wuding);
        music2.setCoverPath(R.drawable.heart_red);
        music2.setSingerName("null");
        musicList.add(music2);
        Music music3 = new Music();
        music3.setMusicName("起风了");
        music3.setMusicPath(R.raw.qifengle);
        music3.setCoverPath(R.drawable.btn_circle);
        music3.setSingerName("卖辣椒也用券");
        musicList.add(music3);
        Music music4 = new Music();
        music4.setMusicName("屋顶");
        music4.setMusicPath(R.raw.wuding);
        music4.setCoverPath(R.drawable.heart_red);
        music4.setSingerName("null");
        musicList.add(music4);
        Music music5 = new Music();
        music5.setMusicName("起风了");
        music5.setMusicPath(R.raw.qifengle);
        music5.setCoverPath(R.drawable.btn_circle);
        music5.setSingerName("卖辣椒也用券");
        musicList.add(music5);
        listIterator = musicList.listIterator();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建音乐播放器对象
        player = null;
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
                if( !MusicPlayerActivity.ifAlive ) return;
                int duration = player.getDuration();//获取歌曲总时长
                int currentPosition = player.getCurrentPosition();//获取播放进度
                Message msg= MusicPlayerActivity.handler.obtainMessage();//创建消息对象

                //todo 先写MusicPlayerActivity
//                    Message msg= MusicPlayerActivity.handler.obtainMessage();//创建消息对象
                //将音乐的总时长和播放进度封装至bundle中
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                bundle.putInt("cover",  nowMusic.getCoverPath());
                bundle.putString("songName", nowMusic.getMusicName());
                //再将bundle封装到msg消息对象中
                msg.setData(bundle);
                //最后将消息发送到主线程的消息队列
                MusicPlayerActivity.handler.sendMessage(msg);
                }
            };
            timer.schedule(timerTask, 5,500);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 内部类：跨进程通信音乐信息的辅助类
     */
   public class MusicControl extends Binder {
       public void refreshList(List<Music> inputMusicList ){
           musicList.clear();
           for( Music f : inputMusicList ){
               musicList.add(f);
           }
           listIterator = musicList.listIterator();
       }

        public void play() {
           if( songThread != null ){
               songThread.interrupt();
               songThread = null;
           }

            songThread = new Thread(() -> {
                while ( listIterator.hasNext() && !Thread.interrupted() ){
                    if( player == null || player.getDuration() <= player.getCurrentPosition() ){
                        nextMusic();
                    }
                    else{
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.d("gzc", "线程停止");
                        }
                    }
                }
            });
            songThread.start();
        }
        //调用MediaPlayer自带的方法实现音乐的暂停、继续和退出

        public void nextMusic(){
            if( songThread != null ){
                songThread.interrupt();
                songThread = null;
            }
            if( listIterator.hasNext() ){
                stopTimer();
                if( player != null ){
                    player.reset();
                }
                if( !listIterator.hasPrevious() ){
                    listIterator.next();
                }
                nowMusic = listIterator.next();
                player = MediaPlayer.create(MusicService.this, nowMusic.getMusicPath());
                player.start();
                addTimer();//添加计时器
            }
        }

        public void previousMusic(){
           stopTimer();
            if( songThread != null ){
                songThread.interrupt();
                songThread = null;
            }
            if( listIterator.hasPrevious() ){
                if( player != null ){
                    player.reset();
                }
                if( !listIterator.hasNext() ){
                    listIterator.previous();
                }
                nowMusic = listIterator.previous();
                player = MediaPlayer.create(MusicService.this, nowMusic.getMusicPath());
                player.start();
                addTimer();//添加计时器
            }
        }

        public void addNextMusic( Music music ){
           listIterator.add(music);
        }

        public boolean getMusicState(){
           if( musicIsNull() ){
               return false;
           }
           return player.isPlaying();
        }

        public Music getNowMusic(){
           return nowMusic;
        }

        public boolean musicIsNull(){
           return player == null;
        }

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
        if (player == null){
            return;
        }
        if (player.isPlaying()){
            player.stop();//停止播放音乐
        }
        player.release();//释放占用的资源
        player = null; //player置为空
    }
}
