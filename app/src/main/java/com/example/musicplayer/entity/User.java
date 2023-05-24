package com.example.musicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 */
public class User {
    private String user_email;//用户邮箱
    private String password;//密码
    private String nickname;//昵称
    private Integer headPicturePath;//头像
    private Integer musicListenTime;//听歌时长
    private List<PlayList> ownerMusicList; //用户拥有的歌单

    /**
     * 无参构造方法
     */
    public User() {
        this.setOwnerMusicList(new ArrayList<>());
    }

    /**
     * 用户名和密码的有参构造
     * @param user_email 用户名
     * @param password 密码
     */
    public User(String user_email, String password) {
        this.setUser_email(user_email);
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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
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

    public int getHeadPicturePath() {
        return headPicturePath;
    }

    public void setHeadPicturePath(Integer headPicturePath) {
        this.headPicturePath = headPicturePath;
    }

    public Integer getMusicListenTime() {
        return musicListenTime;
    }

    public void setMusicListenTime(Integer musicListenTime) {
        this.musicListenTime = musicListenTime;
    }

}
