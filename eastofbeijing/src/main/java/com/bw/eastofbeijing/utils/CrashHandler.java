package com.bw.eastofbeijing.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dash on 2018/2/1.
 *
 * 自己的异常处理器
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler;
    private Context context;
    private Thread.UncaughtExceptionHandler defaultHandler;
    //放崩溃日志的文件夹
    private File logDir = new File(Environment.getExternalStorageDirectory(),"crash");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 对外提供获取实例对象的方法....单例模式
     * @return
     */
    public static CrashHandler getInstance(){

        if (crashHandler == null){
            synchronized (CrashHandler.class){
                if (crashHandler == null){
                    crashHandler = new CrashHandler();
                }
            }
        }

        return crashHandler;
    }

    /**
     * 在外面需要初始化.....application下面调用初始化的方法
     * @param context
     */
    public void init(Context context){
        this.context = context;

        //先拿到系统默认的异常处理器...当发生异常并且没有捕获的时候,系统默认是程序崩溃
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        //设置当前作为我们处理异常的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    //处理发生崩溃的异常....日志写入sd卡...发送到服务器
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        //自己没有处理异常,交给系统来处理
        if (!handlerException(throwable) && defaultHandler != null){
            //交给系统来处理....如果自己没有处理,让程序去崩溃
            defaultHandler.uncaughtException(thread,throwable);
        }else {
            //已经处理了异常....把日志上传服务器...退出应用

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //否则自己处理了异常....上传服务器
            uploadLogToServer();
            //结束进程 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());//杀死应用的进程
            //非正常退出应用....0表示正常退出,,,非0表示不正常
            System.exit(1110);
        }

    }

    /**
     * 上传文件的方法
     */
    private void uploadLogToServer() {

        //1.把sd卡下面crash文件夹下的所有文件,遍历使用okhttp上传到服务器

        //2.上传完成之后删除crash文件夹下所有的文件

    }

    /**
     * 这个方法里面自己处理异常....写入sd卡
     * @param throwable
     * @return
     */
    private boolean handlerException(Throwable throwable) {
        if (throwable == null){
            return false;
        }

        //吐司提示一下
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,"程序出现异常,即将退出",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        //日志写到文件中去
        if (! logDir.exists()){
            logDir.mkdirs();
        }

        //文件夹中创建文件.crash文件夹下面的..crash-2017-12-07-HH-mm-ss-SSS.log
        File logFile = new File(logDir,"crash-"+dateFormat.format(new Date())+"-.log");
        if (!logFile.exists()){
            try {
                logFile.createNewFile();//创建新的文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //写入sd卡...使用打印流
        try {
            PrintWriter printWriter = new PrintWriter(logFile);

            collectLogInfoToSdCard(printWriter,throwable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;

    }

    /**
     * 打印错误信息到sd卡的文件中
     * @param printWriter
     * @param throwable 这个对象中就有错误的信息
     */
    private void collectLogInfoToSdCard(PrintWriter printWriter, Throwable throwable) {

        //日志里面可能会哟版本号 版本名称 时间

        //获取pakage管理者对象
        PackageManager packageManager = context.getPackageManager();
        //获取当前应用的信息....(context.getPackageName()获取当前应用的包名
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            printWriter.print("版本:");
            printWriter.println(packageInfo.versionCode);
            printWriter.print("版本名称:");
            printWriter.println(packageInfo.versionName);

            printWriter.print("time:");
            printWriter.println(dateFormat.format(new Date()));

            //把日志信息放到打印流中去打印
            throwable.printStackTrace(printWriter);

            printWriter.close();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
