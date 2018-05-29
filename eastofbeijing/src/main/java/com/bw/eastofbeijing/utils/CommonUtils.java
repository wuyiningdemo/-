package com.bw.eastofbeijing.utils;

/**
 * Created by Dash
 */
import android.content.SharedPreferences;

import com.bw.eastofbeijing.view.MyApplication;


/**
 * Created by Dash
 */
public class CommonUtils {

    public static final String TAG = "Dash";//sp文件的xml名称
    private static SharedPreferences sharedPreferences;

    /**
     * 自己写的运行在主线程的方法
     * 如果是主线程,执行任务,否则使用handler发送到主线程中去执行
     *
     *
     * @param runable
     */
    public static void runOnUIThread(Runnable runable) {
        //先判断当前属于子线程还是主线程
        if (android.os.Process.myTid() == MyApplication.getMainThreadId()) {
            runable.run();
        } else {
            //子线程
            MyApplication.getAppHanler().post(runable);
        }
    }


    /**
     * sp存入字符串类型的值
     * @param flag
     * @param str
     */
    public static void saveString(String flag, String str) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(TAG, MyApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(flag, str);
        edit.commit();
    }

    public static String getString(String flag) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(TAG, MyApplication.getAppContext().MODE_PRIVATE);
        }
        return sharedPreferences.getString(flag, "");
    }

    public static boolean getBoolean(String tag) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(TAG, MyApplication.getAppContext().MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(tag, false);
    }

    public static void putBoolean(String tag, boolean content) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(TAG, MyApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(tag, content);
        edit.commit();
    }

    /**
     * 清除sp数据
     * @param tag
     */
    public static void clearSp(String tag) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(TAG, MyApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(tag);
        edit.commit();
    }
}
