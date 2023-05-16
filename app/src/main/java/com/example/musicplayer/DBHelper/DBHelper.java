package com.example.musicplayer.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //数据库名&数据库版本
    private static final String DATABASE_NAME = "music_player.db";
    private static final int DATABASE_VERSION = 1;

    //用户表
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_NICKNAME = "nickname";
    private static final String COLUMN_USER_HEAD_PICTURE_PATH = "headPicturePath";
    private static final String COLUMN_USER_MUSIC_LISTEN_TIME = "musicListenTime";

    //音乐表
    private static final String TABLE_MUSIC = "music";
    private static final String COLUMN_MUSIC_ID = "id";
    private static final String COLUMN_MUSIC_NAME = "musicName";
    private static final String COLUMN_MUSIC_SINGER_NAME = "singerName";
    private static final String COLUMN_MUSIC_COVER_PATH = "coverPath";
    private static final String COLUMN_MUSIC_MUSIC_VIDEO_PATH = "musicVideoPath";
    private static final String COLUMN_MUSIC_LYRICS_PATH = "lyricsPath";

    //歌单表
    private static final String TABLE_PLAYLIST = "playlist";
    private static final String COLUMN_PLAYLIST_ID = "id";
    private static final String COLUMN_PLAYLIST_LIST_NAME = "listName";
    private static final String COLUMN_PLAYLIST_LIST_PICTURE_PATH = "listPicturePath";
    private static final String COLUMN_PLAYLIST_OWNER = "owner";

    //创建歌单歌曲表
    private static final String TABLE_PLAYLIST_SONG = "playlistSong";
    private static final String COLUMN_PLAYLIST_SONG_ID = "playlist_id";

    // 创建用户歌单表
    private static final String TABLE_USER_PLAYLIST = "user_playlist";
    private static final String COLUMN_USER_PLAYLIST_ID = "user_playlist_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1.创建用户表
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT NOT NULL," +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
                COLUMN_USER_NICKNAME + " TEXT DEFAULT '未命名'," +
                COLUMN_USER_HEAD_PICTURE_PATH + " TEXT," +
                COLUMN_USER_MUSIC_LISTEN_TIME + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(createUserTableQuery);


        // 2.创建音乐表
        String createMusicTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_MUSIC + "(" +
                COLUMN_MUSIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MUSIC_NAME + " TEXT," +
                COLUMN_MUSIC_SINGER_NAME + " TEXT," +
                COLUMN_MUSIC_COVER_PATH + " TEXT," +
                COLUMN_MUSIC_MUSIC_VIDEO_PATH + " TEXT," +
                COLUMN_MUSIC_LYRICS_PATH + " TEXT" +
                ")";
        db.execSQL(createMusicTableQuery);

        // 3.创建歌单表
        String createPlaylistTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLIST + "(" +
                COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PLAYLIST_LIST_NAME + " TEXT," +
                COLUMN_PLAYLIST_LIST_PICTURE_PATH + " TEXT," +
                COLUMN_PLAYLIST_OWNER + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_PLAYLIST_OWNER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createPlaylistTableQuery);

        // 4.创建歌单歌曲表
        String createPlaylistSongTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLIST_SONG + "(" +
                COLUMN_PLAYLIST_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PLAYLIST_ID + " INTEGER," +
                COLUMN_MUSIC_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_PLAYLIST_ID + ") REFERENCES " + TABLE_PLAYLIST + "(" + COLUMN_PLAYLIST_ID + ")," +
                "FOREIGN KEY(" + COLUMN_MUSIC_ID + ") REFERENCES " + TABLE_MUSIC + "(" + COLUMN_MUSIC_ID + ")" +
                ")";
        db.execSQL(createPlaylistSongTableQuery);

        // 5.创建用户歌单表
        String createUserPlaylistTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_PLAYLIST + "(" +
                COLUMN_USER_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_PLAYLIST_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")," +
                "FOREIGN KEY(" + COLUMN_PLAYLIST_ID + ") REFERENCES " + TABLE_PLAYLIST + "(" + COLUMN_PLAYLIST_ID + ")" +
                ")";
        db.execSQL(createUserPlaylistTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // 向用户表中插入数据
    public long insertUser(String username, String password, String headPicturePath, long musicListenTime) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_HEAD_PICTURE_PATH, headPicturePath);
        values.put(COLUMN_USER_MUSIC_LISTEN_TIME, musicListenTime);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result;
    }

    // 向音乐表中插入数据
    public long insertMusic(String musicName, String singerName, String coverPath, String musicVideoPath, String lyricsPath) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSIC_NAME, musicName);
        values.put(COLUMN_MUSIC_SINGER_NAME, singerName);
        values.put(COLUMN_MUSIC_COVER_PATH, coverPath);
        values.put(COLUMN_MUSIC_MUSIC_VIDEO_PATH, musicVideoPath);
        values.put(COLUMN_MUSIC_LYRICS_PATH, lyricsPath);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_MUSIC, null, values);
        db.close();
        return result;
    }

    // 向歌单表中插入数据
    public long insertPlaylist(String listName, String listPicturePath, long owner) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_LIST_NAME, listName);
        values.put(COLUMN_PLAYLIST_LIST_PICTURE_PATH, listPicturePath);
        values.put(COLUMN_PLAYLIST_OWNER, owner);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_PLAYLIST, null, values);
        db.close();
        return result;
    }

    // 向歌单歌曲表中插入数据
    public long insertPlaylistSong(long playlistId, long musicId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID, playlistId);
        values.put(COLUMN_MUSIC_ID, musicId);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_PLAYLIST_SONG, null, values);
        db.close();
        return result;
    }

    // 向用户歌单表中插入数据
    public long insertUserPlaylist(long userId, long playlistId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PLAYLIST_ID, playlistId);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER_PLAYLIST, null, values);
        db.close();
        return result;
    }

}
