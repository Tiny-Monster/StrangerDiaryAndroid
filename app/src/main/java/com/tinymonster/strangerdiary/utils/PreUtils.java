package com.tinymonster.strangerdiary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;

/**
 * 缓存工具类
 * Created by TinyMonster on 10/01/2019.
 */

public class PreUtils {
    private static String pre_name="user_info";
    private static Context mContext;

    public static void init(Context context){
        mContext=context;
    }

    public static SharedPreferences getSharedPreference(){
        if(mContext==null)
            throw new IllegalStateException("SharedPreferences have not initialized");
        return mContext.getSharedPreferences(pre_name,Context.MODE_PRIVATE);
    }

    public static void put(String key,Object value){
        SharedPreferences sharedPreferences=getSharedPreference();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(value instanceof String){
            editor.putString(key,(String) value);
        }else if(value instanceof Integer){
            editor.putInt(key,(Integer) value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key,(Boolean)value);
        }else if(value instanceof Float){
            editor.putFloat(key,(Float)value);
        }else if(value instanceof Long){
            editor.putLong(key,(Long)value);
        }
        editor.apply();
    }

    public static Object get(String key, Object defauleValue) {
        SharedPreferences sp = getSharedPreference();
        if (defauleValue instanceof String) {
            return sp.getString(key, (String) defauleValue);
        } else if (defauleValue instanceof Integer) {
            return sp.getInt(key, (Integer) defauleValue);
        } else if (defauleValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defauleValue);
        } else if (defauleValue instanceof Float) {
            return sp.getFloat(key, (Float) defauleValue);
        } else if (defauleValue instanceof Long) {
            return sp.getLong(key, (Long) defauleValue);
        }
        return null;
    }

    public static void remove(String remove_key){
        SharedPreferences sharedPreferences=getSharedPreference();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(remove_key);
        editor.apply();
    }

    public static void clearAll() {
        SharedPreferences sp = getSharedPreference();
        SharedPreferences.Editor et = sp.edit();
        et.clear();
        et.apply();
    }
}
