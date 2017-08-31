package com.heping.webcollector.example;


import com.heping.webcollector.util.Config;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Spider on 2017/8/8.
 */
public class TimerManager {

    //凤凰公益图片保存路径
    public static String COM_IF_PATH=null;
    //河南慈善网图片保存路径
    public static String COM_HN_PATH=null;
    //网易新闻图片保存路径
    public static String COM_WY_PATH=null;

    public static void main(String[] args){
        new TimerManager();
    }
    //定时采集
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    public TimerManager() {
        Calendar calendar = Calendar.getInstance();
       /* calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);*/
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
       /* if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }*/
       //图片文件夹创建,每天一次
        mkDir(date);

        Timer timer = new Timer();

        //凤凰公益任务
        IFTask task = new IFTask();
        //河南慈善网
        HNTask hnTask = new HNTask();
        //网易新闻
        WYTask wyTask = new WYTask();

        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task,date,PERIOD_DAY);
        timer.schedule(hnTask,date,PERIOD_DAY);
        timer.schedule(wyTask,date,PERIOD_DAY);
    }
    //创建文件夹
    private void mkDir(Date date) {
        COM_IF_PATH= Config.IMG_DOWNLOAD_IF+new SimpleDateFormat("yyyy/MM/dd").format(date)+"/";
        File file = new File(COM_IF_PATH);
        if (!file.exists()){
            file.mkdirs();
        }
        COM_HN_PATH=Config.IMG_DOWNLOAD_HN+new SimpleDateFormat("yyyy/MM/dd").format(date)+"/";
        if (!file.exists()){
            file.mkdirs();
        }
        COM_WY_PATH=Config.IMG_DOWNLOAD_WY+new SimpleDateFormat("yyyy/MM/dd").format(date)+"/";
        if (!file.exists()){
            file.mkdirs();
        }
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

}
