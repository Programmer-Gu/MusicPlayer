package com.example.musicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 */
public class User {
    private String username;//用户名
    private String password;//密码
    private String nickname;//昵称
    private String headPicturePath;//头像
    private String musicListenTime;//听歌时长
    private List<PlayList> ownerMusicList; //用户拥有的歌单

    /**
     * 无参构造方法
     */
    public User() {
        this.setOwnerMusicList(new ArrayList<>());
    }

    /**
     * 用户名和密码的有参构造
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setOwnerMusicList(new ArrayList<>());
    }


    //--setter and getter

    public List<PlayList> getOwnerMusicList() {
        return ownerMusicList;
    }

    public void setOwnerMusicList(List<PlayList> ownerMusicList) {
        this.ownerMusicList = ownerMusicList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPicturePath() {
        return headPicturePath;
    }

    public void setHeadPicturePath(String headPicturePath) {
        this.headPicturePath = headPicturePath;
    }

    public String getMusicListenTime() {
        return musicListenTime;
    }

    public void setMusicListenTime(String musicListenTime) {
        this.musicListenTime = musicListenTime;
    }

}
