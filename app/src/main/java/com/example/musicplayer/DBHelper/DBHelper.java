package com.example.musicplayer.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.musicplayer.LogHelper.DBLog;
import com.example.musicplayer.R;
import com.example.musicplayer.entity.Music;
import com.example.musicplayer.entity.PlayList;
import com.example.musicplayer.entity.User;

import java.util.ArrayList;
import java.util.List;

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
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_NICKNAME = "nickname";
    private static final String COLUMN_USER_HEAD_PICTURE_PATH = "headPicturePath";
    private static final String COLUMN_USER_MUSIC_LISTEN_TIME = "musicListenTime";

    //音乐表
    private static final String TABLE_MUSIC = "music";
    private static final String COLUMN_MUSIC_ID = "music_id";
    private static final String COLUMN_MUSIC_NAME = "musicName";
    private static final String COLUMN_MUSIC_SINGER_NAME = "singerName";
    private static final String COLUMN_MUSIC_COVER_PATH = "coverPath";
    private static final String COLUMN_MUSIC_PATH = "musicPath";


    //歌单表
    private static final String TABLE_PLAYLIST = "playlist";
    private static final String COLUMN_PLAYLIST_ID = "playlist_id";
    private static final String COLUMN_PLAYLIST_LIST_NAME = "listName";
    private static final String COLUMN_PLAYLIST_LIST_PICTURE_PATH = "listPicturePath";
    private static final String COLUMN_PLAYLIST_OWNER = "owner";

    //创建歌单歌曲表
    private static final String TABLE_PLAYLIST_SONG = "playlistSong";
    private static final String COLUMN_PLAYLIST_SONG_ID = "song_playlist_id";

    // 创建用户歌单表
    private static final String TABLE_USER_PLAYLIST = "user_playlist";
    private static final String COLUMN_USER_PLAYLIST_ID = "user_playlist_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 单例模式获取操作对象
     *
     * @param context 上下文
     * @return DBHelper操作对象
     */
    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }


    /**
     * 打开数据库的读连接
     */
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = dbHelper.getReadableDatabase();
        }
        return mRDB;
    }


    /**
     * 打开数据库的写连接
     */
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = dbHelper.getWritableDatabase();
        }
        return mWDB;
    }


    /**
     * 数据库连接关闭
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
                COLUMN_USER_EMAIL + " TEXT NOT NULL," +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
                COLUMN_USER_NICKNAME + " TEXT DEFAULT '未命名'," +
                COLUMN_USER_HEAD_PICTURE_PATH + " INTEGER DEFAULT 2131165273," +
                COLUMN_USER_MUSIC_LISTEN_TIME + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(createUserTableQuery);


        // 创建音乐表
        String createMusicTableQuery = "CREATE TABLE " + TABLE_MUSIC + "(" +
                COLUMN_MUSIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MUSIC_NAME + " TEXT NOT NULL," +
                COLUMN_MUSIC_SINGER_NAME + " TEXT NOT NULL," +
                COLUMN_MUSIC_COVER_PATH + " INTEGER," +
                COLUMN_MUSIC_PATH + " TEXT)";
        db.execSQL(createMusicTableQuery);


        // 3.创建歌单表
        String createPlaylistTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLIST + "(" +
                COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PLAYLIST_LIST_NAME + " TEXT," +
                COLUMN_PLAYLIST_LIST_PICTURE_PATH + " INTEGER," +
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

    /**
     * 向用户表中插入数据
     *
     * @param user
     * @return
     */
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, user.getUser_email());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER, null, values);

        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_USER, "插入" + result + "行");
        return result;
    }

    /**
     * 向音乐表中插入数据
     *
     * @param music
     * @return
     */
    public long insertMusic(Music music) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSIC_NAME, music.getMusicName());
        values.put(COLUMN_MUSIC_SINGER_NAME, music.getSingerName());
        values.put(COLUMN_MUSIC_PATH, music.getMusicPath());
        values.put(COLUMN_MUSIC_COVER_PATH, music.getCoverPath());
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_MUSIC, null, values);

        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_MUSIC, "插入" + result + "行");
        return result;
    }

    /**
     * 向歌单表中插入数据
     *
     * @param listName
     * @param listPicturePath
     * @param owner
     * @return
     */
    public long insertPlaylist(String listName, Integer listPicturePath, long owner) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_LIST_NAME, listName);
        values.put(COLUMN_PLAYLIST_LIST_PICTURE_PATH, listPicturePath);
        values.put(COLUMN_PLAYLIST_OWNER, owner);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_PLAYLIST, null, values);

        // 获取自增ID
        long insertedId = -1;
        if (result != -1) {
            String query = "SELECT last_insert_rowid()";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                insertedId = cursor.getLong(0);
            }
            cursor.close();
        }


        // 打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_PLAYLIST, "插入" + result + "行，自增ID为" + insertedId);

        return insertedId;
    }

    /**
     * 向歌单歌曲表中插入数据
     *
     * @param playlistId
     * @param musicId
     * @return
     */
    public long insertPlaylistSong(long playlistId, long musicId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID, playlistId);
        values.put(COLUMN_MUSIC_ID, musicId);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_PLAYLIST_SONG, null, values);

        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_PLAYLIST_SONG, "插入" + result + "行");
        return result;
    }

    /**
     * 向用户歌单表中插入数据
     *
     * @param userId
     * @param playlistId
     * @return
     */
    public long insertUserPlaylist(long userId, long playlistId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PLAYLIST_ID, playlistId);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER_PLAYLIST, null, values);

        //打印日志
        DBLog.d(DBLog.INSERT_TAG, TABLE_USER_PLAYLIST, "插入" + result + "行");
        return result;
    }

    /**
     * 用户注册方法，如果邮箱没有被注册过则插入用户数据，否则不插入
     *
     * @param email    用户注册使用的邮箱
     * @param password 用户注册使用的密码
     * @return true:注册成功 false:注册失败
     */
    public boolean registerUser(String email, String password) {

        // 查询用户表
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = this.mRDB.rawQuery(query, new String[]{email});

        boolean isEmailRegistered = cursor.moveToFirst(); // 如果邮箱已经注册，moveToFirst() 返回 true

        cursor.close();

        // 如果邮箱已经注册过，返回 false
        if (isEmailRegistered) {
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "邮箱已存在");
            return false;
        }

        // 如果邮箱未注册，执行插入操作
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        this.mWDB.insert(TABLE_USER, null, values);
        DBLog.d(DBLog.INSERT_TAG, TABLE_USER, String.format("插入成功，用户使用邮箱:{}成功注册", email));
        return true;
    }

    /**
     * 用户登录方法，当且仅当用户的邮箱存在且邮箱密码都正确返回用户id
     *
     * @param context  页面上下文
     * @param email    用户邮箱
     * @param password 用户密码
     */
    public int loginUser(Context context, String email, String password) {

        // 查询用户表
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = this.mRDB.rawQuery(query, new String[]{email});

        boolean isEmailRegistered = cursor.moveToFirst(); // 如果邮箱已经注册，moveToFirst() 返回 true

        // 如果邮箱未注册，返回 -1 表示登录失败
        if (!isEmailRegistered) {
            cursor.close();
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "邮箱不存在");
            showToast(context, "您输入的邮箱尚未注册");
            return -1;
        }

        // 邮箱存在，检查密码是否正确
        int passwordColumnIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
        if (passwordColumnIndex == -1) {
            cursor.close();
            // 处理密码列不存在的情况
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "密码列不存在");
            return -1;
        }

        String storedPassword = cursor.getString(passwordColumnIndex);


        if (!password.equals(storedPassword)) {
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "密码不正确");
            showToast(context, "你输入的密码不正确喔~");
            return -1;
        }

        // 邮箱存在且密码正确，获取用户ID并返回
        int userIdColumnIndex = cursor.getColumnIndex(COLUMN_USER_ID);
        if (userIdColumnIndex == -1) {
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "用户ID列不存在");
            return -1;
        }

        int userId = cursor.getInt(userIdColumnIndex);

        DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "登录成功");
        showToast(context, "恭喜您登录成功！");
        cursor.close();
        return userId;
    }


    /**
     * 根据用户id查询用户的邮箱和昵称
     *
     * @param userId 用户id
     * @return User对象
     */
    public User getUserById(int userId) {
        String query = "SELECT " + COLUMN_USER_NICKNAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_HEAD_PICTURE_PATH +
                " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = mRDB.rawQuery(query, new String[]{String.valueOf(userId)});

        //处理查询结果
        if (cursor.moveToFirst()) {
            int nicknameColumnIndex = cursor.getColumnIndex(COLUMN_USER_NICKNAME);
            int emailColumnIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int headPicturePathIndex = cursor.getColumnIndex(COLUMN_USER_HEAD_PICTURE_PATH);

            if (nicknameColumnIndex != -1 && emailColumnIndex != -1) {
                String nickname = cursor.getString(nicknameColumnIndex);
                String email = cursor.getString(emailColumnIndex);
                int headPicturePath = cursor.getInt(headPicturePathIndex);
                cursor.close();
                User user = new User();
                user.setNickname(nickname);
                user.setUser_email(email);
                user.setHeadPicturePath(headPicturePath);
                return user;
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "列索引无效");
                return null; // 列索引无效
            }
        } else {
            cursor.close();
            DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "用户不存在");
            return null; // 用户不存在
        }
    }

    /**
     * 根据歌曲路径查询歌曲id
     *
     * @param musicPath 用户id
     * @return Integer 歌曲id
     */
    public Integer getMusicIdByPath(int musicPath) {
        String query = "SELECT " + COLUMN_MUSIC_ID +
                " FROM " + TABLE_MUSIC +
                " WHERE " + COLUMN_MUSIC_PATH + " = ?";
        Cursor cursor = mRDB.rawQuery(query, new String[]{String.valueOf(musicPath)});

        //处理查询结果
        if (cursor.moveToFirst()) {

            int musicPathIndex = cursor.getColumnIndex(COLUMN_MUSIC_ID);

            if (musicPathIndex != -1) {

                int musicId = cursor.getInt(musicPathIndex);

                return musicId;
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_MUSIC, "列索引无效");
                return null; // 列索引无效
            }
        } else {
            cursor.close();
            DBLog.d(DBLog.QUERY_TAG, TABLE_MUSIC, "歌曲不存在");
            return null; // 歌曲不存在
        }
    }


    /**
     * 根据用户id查询其歌单id
     *
     * @param userId 用户id
     * @return List<Integer> 存放用户的歌单id
     */
    public List<Integer> getPlaylistByUserId(int userId) {
        String query = "SELECT " + COLUMN_PLAYLIST_ID +
                " FROM " + TABLE_USER_PLAYLIST +
                " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = mRDB.rawQuery(query, new String[]{String.valueOf(userId)});

        List<Integer> list = new ArrayList<>();
        //处理查询结果
        while (cursor.moveToNext()) {
            int playlistIdColumnIndex = cursor.getColumnIndex(COLUMN_PLAYLIST_ID);

            if (playlistIdColumnIndex != -1) {
                int playlistId = cursor.getInt(playlistIdColumnIndex);
                list.add(playlistId);

            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_USER, "列索引无效");
                return null; // 列索引无效
            }
        }
        return list;
    }

    /**
     * 根据歌曲id查询歌曲对象
     *
     * @param musicId 用户id
     * @return Music对象
     */
    public Music getMusicById(int musicId) {
        String query = "SELECT " + COLUMN_MUSIC_NAME + ", " + COLUMN_MUSIC_SINGER_NAME + ", " + COLUMN_MUSIC_COVER_PATH + ", " + COLUMN_MUSIC_PATH +
                " FROM " + TABLE_MUSIC +
                " WHERE " + COLUMN_MUSIC_ID + " = ?";
        Cursor cursor = mRDB.rawQuery(query, new String[]{String.valueOf(musicId)});

        //处理查询结果
        if (cursor.moveToFirst()) {
            int musicNameColumnIndex = cursor.getColumnIndex(COLUMN_MUSIC_NAME);
            int singerColumnIndex = cursor.getColumnIndex(COLUMN_MUSIC_SINGER_NAME);
            int musicCoverPathIndex = cursor.getColumnIndex(COLUMN_MUSIC_COVER_PATH);
            int musicPathIndex = cursor.getColumnIndex(COLUMN_MUSIC_PATH);

            if (musicNameColumnIndex != -1 && singerColumnIndex != -1 && musicCoverPathIndex != -1 && musicPathIndex != -1) {
                String musicName = cursor.getString(musicNameColumnIndex);
                String singer = cursor.getString(singerColumnIndex);
                int musicCoverPath = cursor.getInt(musicCoverPathIndex);
                int musicPath = cursor.getInt(musicPathIndex);
                cursor.close();
                Music music = new Music();
                music.setMusicName(musicName);
                music.setSingerName(singer);
                music.setCoverPath(musicCoverPath);
                music.setMusicPath(musicPath);

                return music;
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_MUSIC, "列索引无效");
                return null; // 列索引无效
            }
        } else {
            cursor.close();
            DBLog.d(DBLog.QUERY_TAG, TABLE_MUSIC, "音乐不存在");
            return null; // 音乐不存在
        }
    }


    /**
     * 根据用户id修改用户昵称
     *
     * @param userId      用户id
     * @param newNickname 用户要修改的昵称
     * @return 数据库user表中被修改的行数
     */
    public int updateNicknameById(int userId, String newNickname) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NICKNAME, newNickname);

        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};

        int rowsAffected = mWDB.update(TABLE_USER, values, whereClause, whereArgs);

        if (rowsAffected == 1) {
            DBLog.d(DBLog.UPDATE_TAG, TABLE_USER, "用户" + userId + "昵称更改成功");
        } else {
            DBLog.d(DBLog.UPDATE_TAG, TABLE_USER, "用户昵称更改失败，有" + rowsAffected + "行数据被修改");
        }
        return rowsAffected;
    }


    /**
     * 根据用户ID修改用户头像路径
     *
     * @param userId             用户ID
     * @param newHeadPicturePath 用户要修改的头像路径
     * @return 数据库user表中被修改的行数
     */
    public int updateHeadPicturePathById(int userId, int newHeadPicturePath) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_HEAD_PICTURE_PATH, newHeadPicturePath);

        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};

        int rowsAffected = db.update(TABLE_USER, values, whereClause, whereArgs);


        if (rowsAffected == 1) {
            DBLog.d(DBLog.UPDATE_TAG, TABLE_USER, "用户" + userId + "头像路径更改成功");
        } else {
            DBLog.d(DBLog.UPDATE_TAG, TABLE_USER, "用户头像路径更改失败，有" + rowsAffected + "行数据被修改");
        }
        return rowsAffected;
    }

    /**
     * 根据歌单id构造歌单对象
     *
     * @param playlistId 歌曲列表id
     * @return PlayList对象
     */
    public PlayList findMusicListById(int playlistId) {

        //创建歌单对象
        PlayList playList = new PlayList();
        //查询歌单的相关信息
        String query = "SELECT " + COLUMN_PLAYLIST_LIST_NAME + ", " + COLUMN_PLAYLIST_LIST_PICTURE_PATH +
                " FROM " + TABLE_PLAYLIST +
                " WHERE " + COLUMN_PLAYLIST_ID + " = ?";

        Cursor cursor = mRDB.rawQuery(query, new String[]{String.valueOf(playlistId)});

        //处理查询结果
        if (cursor.moveToFirst()) {
            int playListNameColumnIndex = cursor.getColumnIndex(COLUMN_PLAYLIST_LIST_NAME);
            int playListPicturePathColumnIndex = cursor.getColumnIndex(COLUMN_PLAYLIST_LIST_PICTURE_PATH);


            if (playListNameColumnIndex != -1 && playListPicturePathColumnIndex != -1) {
                String listName = cursor.getString(playListNameColumnIndex);
                int picturePath = cursor.getInt(playListPicturePathColumnIndex);

                cursor.close();
                playList.setListName(listName);
                playList.setListPicturePath(picturePath);
                List<Music> musicList = new ArrayList<>();
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_PLAYLIST, "列索引无效");
                return null; // 列索引无效
            }
        } else {
            cursor.close();
            DBLog.d(DBLog.QUERY_TAG, TABLE_PLAYLIST, "歌单不存在");
            return null; // 歌单不存在
        }

        //查询歌单所包含的歌
        query = "SELECT " + COLUMN_MUSIC_ID +
                " FROM " + TABLE_PLAYLIST_SONG +
                " WHERE " + COLUMN_PLAYLIST_ID + " = ?";
        cursor = mRDB.rawQuery(query, new String[]{String.valueOf(playlistId)});


        //处理查询结果
        while (cursor.moveToNext()) {

            int musicIdColumnIndex = cursor.getColumnIndex(COLUMN_MUSIC_ID);


            if (musicIdColumnIndex != -1) {
                int musicId = cursor.getInt(musicIdColumnIndex);
                Music tmp_music = dbHelper.getMusicById(musicId);
                playList.getMusicList().add(tmp_music);
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_PLAYLIST_SONG, "列索引无效");
                return null; // 列索引无效
            }
        }

        return playList;
    }


    /**
     * 根据歌名进行模糊查询
     *
     * @param name 歌名
     * @return 所有符合查询的歌曲列表
     */
    public List<Music> searchMusicByName(String name) {
        List<Music> list = new ArrayList<>();
        String query = "SELECT " + COLUMN_MUSIC_ID +
                " FROM " + TABLE_MUSIC +
                " WHERE " + COLUMN_MUSIC_NAME + " LIKE '%' || ? || '%'";
        Cursor cursor = mRDB.rawQuery(query, new String[]{name});

        while (cursor.moveToNext()) {

            int musicIdColumnIndex = cursor.getColumnIndex(COLUMN_MUSIC_ID);

            if (musicIdColumnIndex != -1) {
                int musicId = cursor.getInt(musicIdColumnIndex);
                Music tmp_music = dbHelper.getMusicById(musicId);
                list.add(tmp_music);
            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_PLAYLIST_SONG, "列索引无效");
                return null; // 列索引无效
            }
        }
        return list;
    }

    /**
     * 检查音乐表是否创建过
     *
     * @return
     */
    public boolean MusicTableExists() {
        String checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        Cursor cursor = mRDB.rawQuery(checkTableQuery, new String[]{TABLE_MUSIC});

        if (cursor.moveToFirst()) {

            int musicIdColumnIndex = cursor.getColumnIndex(COLUMN_MUSIC_ID);

            if (musicIdColumnIndex != -1) {
                return true;

            } else {
                cursor.close();
                DBLog.d(DBLog.QUERY_TAG, TABLE_MUSIC, "列索引无效");
                return false; // 列索引无效
            }
        }
        return false;
    }


    /**
     * 获取所有音乐id
     *
     * @return
     */
    public List<Integer> getAllMusicIds() {
        List<Integer> musicIds = new ArrayList<>();

        String[] columns = {COLUMN_MUSIC_ID}; // 列名
        Cursor cursor = mRDB.query(TABLE_MUSIC, columns, null, null, null, null, null);

        int columnIndex = cursor.getColumnIndexOrThrow(COLUMN_MUSIC_ID);
        while (cursor.moveToNext()) {
            int musicId = cursor.getInt(columnIndex);
            musicIds.add(musicId);
        }

        cursor.close();

        return musicIds;
    }


    /**
     * 获取所有音乐对象
     *
     * @return
     */
    public List<Music> getAllMusic() {
        List<Music> list = new ArrayList<>();
        List<Integer> music_id_list = getAllMusicIds();
        for (Integer integer : music_id_list) {
            list.add(getMusicById(integer));
        }
        return list;
    }


    /**
     * toast消息辅助方法
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
