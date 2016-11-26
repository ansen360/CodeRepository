package com.tomorrow_p.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;

import com.tomorrow_p.R;
import com.tomorrow_p.common.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ansen on 2015/10/11 17:58.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class DateFormatActivity extends Activity {

    private static final String TAG = "DateFormat";
    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seelog_activity);
        TextView log = (TextView) findViewById(R.id.log);
        mStringBuilder = new StringBuilder();
        init();
        log.setText(mStringBuilder.toString());
    }

    private void init() {

        // ------------------SimpleDateFormat----------------------------
        // 方式一
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
                "yyyy年MM月dd日HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        String str1 = simpleDateFormat1.format(date1);
        log(str1);

        // 方式二
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
                "yyyy-MM-dd   hh:mm:ss");
        String date2 = simpleDateFormat2.format(new Date());
        log(date2);

        // 只获取 年 月 （只获取时间或秒同理）
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM");
        String str3 = simpleDateFormat3.format(new java.util.Date());
        log(str3);

        // 指定时区的时间
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
        String str4 = dateTimeInstance.format(new Date());
        log(str4);


        // -----------------获取时间是24小时制还是12小时制-----------------------------

        ContentResolver cv = this.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);

        log(strTimeFormat);

        // ---------------------calendar---------------------------

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        log("year:" + year + "_month:" + month + "_day:" + day
                + "_hour:" + hour + "_minute:" + minute + "_SECOND:" + second);

        // ------------------------Time--------------------------------
        Time t = new Time(); // Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year1 = t.year;
        int month1 = t.month;
        int day1 = t.monthDay;
        int hour1 = t.hour; // 0-23
        int minute1 = t.minute;
        int second1 = t.second;

        log("year1:" + year1 + "_month1:" + month1 + "_day1:"
                + day1 + "_hour1:" + hour1 + "_minute1:" + minute1
                + "_SECOND1:" + second1);

    }

    private void log(String log) {
        Logger.i(log);
        mStringBuilder.append(log + "\n");
    }
}
