package com.qixi.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 83642 on 2016/8/8.
 */
public class SharUtil {

    private static final String GIF = "gif";

    public static void recevieGif(int index, Context context){
        SharedPreferences preferences = context.getSharedPreferences(GIF, Activity.MODE_PRIVATE);
        preferences.edit().putInt("index",index).apply();
    }

    public static int getGif( Context context){
        SharedPreferences preferences = context.getSharedPreferences(GIF, Activity.MODE_PRIVATE);
        return  preferences.getInt("index",0);
    }

}
