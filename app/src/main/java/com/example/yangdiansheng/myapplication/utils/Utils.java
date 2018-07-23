package com.example.yangdiansheng.myapplication.utils;

import android.animation.ObjectAnimator;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangdiansheng on 2018/7/23.
 */

public class Utils {
    public static String getTime(long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }

    public static void reserveView(View view, int from, int to) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", from, to);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
}
