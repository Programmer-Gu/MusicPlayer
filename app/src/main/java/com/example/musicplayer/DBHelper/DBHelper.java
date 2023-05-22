package com.example.musicplayer.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.musicplayer.LogHelper.DBLog;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.User;

public class DBHelper extends SQLiteOpenHelper {

    //数据库名&数据库版本
    private static final String DATABASE_NAME = "music_player.db";
    private static final int DATABASE_VERSION = 1;

    //数据库操作对象
    private static DBHelper dbHelper = null;
    private SQLiteDatabase mRDB = null; //数据库读对象
    private SQLiteDatabase mWDB = null; //数据库写对象


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

    /**
     * 单例模式获取操作对象
     * @param context  上下文
     * @return DBHelper操作对象
     */
    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }


    /**
     *  打开数据库的读连接
     */
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = dbHelper.getReadableDatabase();
        }
        return mRDB;
    }


    /**
     *  打开数据库的写连接
     */
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = dbHelper.getWritableDatabase();
        }
        return mWDB;
    }


    /**
     *  数据库连接关闭
     */
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
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
                COLUMN_MUSIC_NAME + " TEXT NOT NULL," +
                COLUMN_MUSIC_SINGER_NAME + " TEXT NOT NULL," +
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
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_USER, "插入" + result + "行");
        return result;
    }

    // 向音乐表中插入数据
    public long insertMusic(Music music) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSIC_NAME, music.getMusicName());
        values.put(COLUMN_MUSIC_SINGER_NAME, music.getSingerName());

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_MUSIC, null, values);
        db.close();
        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_MUSIC, "插入" + result + "行");
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
        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_PLAYLIST, "插入" + result + "行");
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
        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_PLAYLIST_SONG, "插入" + result + "行");
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
        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_USER_PLAYLIST, "插入" + result + "行");
        return result;
    }

}