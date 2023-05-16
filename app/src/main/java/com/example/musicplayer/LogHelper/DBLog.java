package com.example.musicplayer.LogHelper;


import android.util.Log;

/**
 * 本工具类用于数据库操作的日志大打印
 */

public class DBLog {
    public static final String INSERT_TAG = "数据库插入操作";
    public static final String DELETE_TAG = "数据库删除操作";
    public static final String UPDATE_TAG = "数据库修改操作";
    public static final String QUERY_TAG = "数据库查找操作";
    private static boolean isLoggingEnabled = true; // 控制日志输出开关
    // 设置日志输出开关

    public static void setLoggingEnabled(boolean enabled) {
        isLoggingEnabled = enabled;
    }

    // 输出调试信息
    public static void d(String TAG,String TABLE_NAME,String message) {
        if (isLoggingEnabled) {
            Log.d(TAG, "["+TABLE_NAME+"]表完成操作:"+message);
        }
    }

    // 输出错误信息
    public static void e(String TAG,String TABLE_NAME,String message) {
        if (isLoggingEnabled) {
            Log.d(TAG, "["+TABLE_NAME+"]表操作错误:"+message);
        }
    }





}
