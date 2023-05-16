package com.example.musicplayer.entity;


/**
 * 音乐实体类
 */
public class Music {
    private String musicName;//歌名
    private String singerName;//歌手名
    private String coverPath;//音乐封面的路径
    private String musicVideoPath;//MV路径
    private String lyricsPath;//歌词路径

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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getMusicVideoPath() {
        return musicVideoPath;
    }

    public void setMusicVideoPath(String musicVideoPath) {
        this.musicVideoPath = musicVideoPath;
    }

    public String getLyricsPath() {
        return lyricsPath;
    }

    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath;
    }

}
