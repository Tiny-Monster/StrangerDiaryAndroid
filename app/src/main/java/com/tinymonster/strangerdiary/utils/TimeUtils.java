package com.tinymonster.strangerdiary.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TinyMonster on 12/02/2019.
 */

public class TimeUtils {
    public static String getFormatTime(String format){
        Date date=new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        String formatTime=dateFormat.format(date);
        return formatTime;
    }
}
