package com.bw.eastofbeijing.view;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bw.eastofbeijing.model.db.AddrDao;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Dash on 2018/3/16.
 */
public class MyApplication extends Application {

    private static int width;
    private static int height;
    private static int myTid;
    private static Handler handler;
    private static Context context;
    /**
     * 实际开发中 这些平台的id,key,secret全都是要去对应的开放平台申请
     */
    {

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        getDeviceHeightAndWidth();


        myTid = Process.myTid();
        handler = new Handler();
        context = getApplicationContext();


        //初始化自己的异常捕获机制
        //CrashHandler.getInstance().init(this);

        //如果要使用腾讯的bugly处理异常...必须把自己的异常处理给注释掉

        //初始化友盟
        UMShareAPI.get(this);
        Config.DEBUG = true;//开启debug

        //初始化地址的数据库
       new AddrDao(this).initAddrDao();

        //初始化zxing库
        ZXingLibrary.initDisplayOpinion(this);


    }

    /**
     * 获取屏幕的宽高
     */
    private void getDeviceHeightAndWidth() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        // 屏幕宽度（像素）
        width = dm.widthPixels;
        // 屏幕高度（像素）
        height = dm.heightPixels;

    }
    //获取主线程的id
    public static int getMainThreadId() {

        return myTid;
    }

    //获取handler
    public static Handler getAppHanler() {
        return handler;
    }

    public static Context getAppContext() {
        return context;
    }
    /**
     * 对外提供获取屏幕宽度的方法
     */
    public static int getDeviceWidth(){
        return width;
    }

    public static int getDeviceHeight(){
        return height;
    }
}
