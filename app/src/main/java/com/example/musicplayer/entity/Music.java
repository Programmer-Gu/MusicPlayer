package com.example.musicplayer.entity;


import android.net.Uri;

/**
 * 音乐实体类
 */
public class Music {
    private String musicName;//歌名
    private String singerName;//歌手名


    public Music() {

    }


    //--setter and getter


    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }



}
