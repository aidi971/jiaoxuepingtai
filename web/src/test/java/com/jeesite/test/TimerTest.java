package com.jeesite.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    public static void main(String[] args) {
        System.out.println(formatTime(System.currentTimeMillis()+""));
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 调用识别结果进度查询接口
                System.out.println(formatTime(System.currentTimeMillis() + ""));
            }
        }, 10000);//延迟3秒执行一次，然后每个2秒执行一次
        System.out.println();
    }
    private static Date formatTime(String d) {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date=simpleDateFormat.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
