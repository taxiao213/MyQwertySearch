package com.hr.han.utils.text;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hr.han.R;
import com.hr.han.utils.UIUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间工具类
 * Created by zhangjutao on 2015/7/14.
 */
public class TimeUtil {

    public static final int DAY = 1;
    public static final int HOUR = 2;
    public static final int MIN = 3;
    public static final int SEC = 4;



    public static String getTimeForPlayer(long current) {
        current /= 1000;
        int currentInt = (int) current;
        int minute = currentInt / 60;
        int hour = currentInt / 60;
        int second = currentInt % 60;
        minute %= 60;
        if (hour == 0) {
            return String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d", minute, second);
        }
        return String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * 设置Button倒数读秒
     *
     * @param btn  button
     * @param time 时间(秒)
     */
    public static CountDownTimer setButtonCoolingDown(final TextView btn, int time, int sendFinishDrawable
            , final int sendAgDrawable) {
        if (btn.isClickable()) {
            btn.setClickable(false);
            btn.setBackgroundDrawable(UIUtil.getDrawable(sendFinishDrawable));
            return new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int remaining = (int) (millisUntilFinished / 1000);
                    btn.setText(String.format("重新发送(%s)", remaining));
                }

                @Override
                public void onFinish() {
                    btn.setText(UIUtil.getString(R.string.send_code_again));
                    btn.setClickable(true);
                    btn.setBackgroundDrawable(UIUtil.getDrawable(sendAgDrawable));
                }
            }.start();
        }

        return null;
    }

    public static String getCurrentTime() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss",Locale.SIMPLIFIED_CHINESE);
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return getTimeFormat(System.currentTimeMillis());
    }

    public static String getTimeFormat(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);
        Date curDate = new Date(timeMillis);//获取当前时间
        return formatter.format(timeMillis);
    }

    /** 根据时间戳获取月份的某一天 */
    public static int getDay(long time) {
        return getCalendar(time).get(Calendar.DAY_OF_MONTH);
    }

    @NonNull
    private static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    /** 根据时间戳获取月份 */
    public static int getMonth(long time) {
        return getCalendar(time).get(Calendar.MONTH);
    }

    public static int getYear(long time) {
        return getCalendar(time).get(Calendar.YEAR);
    }

    public static long getTimeLong(long time1,long time2,int type){
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long back = 0;
        long diff = 0;
        if (time1<time2){
            diff = time2-time1;
        }else {
            diff = time1-time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        switch (type) {
            case DAY:
                back = day;
                break;
            case HOUR:
                back = hour;
                break;
            case MIN:
                back = min;
                break;
            case SEC:
                back = sec;
                break;
        }
        return back;
    }


    /**
     * 当前时间是一周中的第几天，外国周日为第一天，换算
     * @return
     */
    public static int getDayInWeek(){
        Calendar calendar= Calendar.getInstance();
        //获取当前时间为本周的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            day=7;
        } else {
            day=day-1;
        }
        return day;
    }

    public static int getHourOfDay(){
        Calendar calendar= Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static int getMinOfHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 当前周是一个月中的第几周，外国周日为第一天，换算
     * @return
     */
    public int getWeekInMouth(){
        Calendar calendar= Calendar.getInstance();
        //获取当前时间为本月的第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            week=week-1;
        }
        return week;
    }

    public static String getDateAfter(long times, int day) {
        Date date = new Date(times);
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(now.getTime());
    }


}
