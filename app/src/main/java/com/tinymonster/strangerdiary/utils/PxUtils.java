package com.tinymonster.strangerdiary.utils;


import com.tinymonster.strangerdiary.application.AppContext;

/**
 * Created by TinyMonster on 31/12/2018.
 */

public class PxUtils {
    public static int Dip2Px(float dipValue){
        float m= AppContext.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue*m+0.5f);
    }

}
