package com.example.musicplayer.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 歌单类
 */
public class PlayList {
    private String listName;//歌单名
    private Integer listPicturePath;//歌单封面路径
    private Integer owner;//歌单所属用户
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
     * @param user_id 用户ID
     */
    public PlayList(Integer user_id) {
        this.setOwner(user_id);
        this.setListName("未命名");
        this.setMusicList(new ArrayList<>());
    }

    /**
     * 有参构造方法
     *
     * @param user_id 用户ID
     * @param listName 歌单名
     */
    public PlayList(Integer user_id, String listName) {
        this.setOwner(user_id);
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

    public Integer getListPicturePath() {
        return listPicturePath;
    }

    public void setListPicturePath(Integer listPicturePath) {
        this.listPicturePath = listPicturePath;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }


}
