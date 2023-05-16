package com.example.musicplayer.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 歌单类
 */
public class PlayList {
    private String listName;//歌单名
    private String listPicturePath;//歌单封面路径
    private String owner;//歌单所属用户
    private List<Music> musicList;//歌曲列表

    /**
     * 无参构造方法
     */
    public PlayList() {
        this.setListName("未命名");
        this.setMusicList(new ArrayList<>());
    }

    /**
     * 有参构造方法
     *
     * @param username 用户名（注意是账号而不是昵称）
     */
    public PlayList(String username) {
        this.setOwner(username);
        this.setListName("未命名");
        this.setMusicList(new ArrayList<>());
    }

    /**
     * 有参构造方法
     *
     * @param username 用户名（注意是账号而不是昵称）
     * @param listName 歌单名
     */
    public PlayList(String username, String listName) {
        this.setOwner(username);
        this.setListName(listName);
        this.setMusicList(new ArrayList<>());
    }



    //--setter and getter

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListPicturePath() {
        return listPicturePath;
    }

    public void setListPicturePath(String listPicturePath) {
        this.listPicturePath = listPicturePath;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }


}
