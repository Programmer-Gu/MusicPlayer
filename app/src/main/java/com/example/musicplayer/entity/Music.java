package com.example.musicplayer.entity;


import android.net.Uri;

/**
 * 音乐实体类
 */
public class Music {
    private String musicName;//歌名
    private String singerName;//歌手名

    private Integer musicPath;//歌曲路径

    private Integer coverPath;//封面路径

    public Music() {}


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

    public Integer getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(Integer musicPath) {
        this.musicPath = musicPath;
    }

    public Integer getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(Integer coverPath) {
        this.coverPath = coverPath;
    }

}