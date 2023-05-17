package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicPlayerActivity extends AppCompatActivity {
    //进度条
    private static SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
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
}