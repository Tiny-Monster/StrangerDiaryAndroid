package com.tinymonster.strangerdiary.utils;

import android.content.Context;
import android.widget.Toast;

/**，当Toast的msg与上一次相同且与上一次的时间间隔小于设置值的时候不Toast
 * Created by TinyMonster on 04/01/2019.
 */

public class ToastUtils {
    private static String oldMsg;
    private static Toast toast=null;
    private static long oneTime=0;
    private static long twoTime=0;

    public static void showToast(Context context,String s){
        if(toast==null){
            toast=Toast.makeText(context,s,Toast.LENGTH_LONG);
            toast.show();
            oldMsg=s;
            oneTime=System.currentTimeMillis();
        }else {
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_LONG){
                    toast.show();
                }
            }else {
                oldMsg=s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
