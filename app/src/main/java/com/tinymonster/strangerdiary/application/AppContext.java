package com.tinymonster.strangerdiary.application;

import android.content.Context;

/**AppContext 单例模式
 * Created by TinyMonster on 03/01/2019.
 */

public class AppContext {
    private static Context mContext;
    private static AppContext mInstance;

    private AppContext(Context context){
        mContext=context;
    }

    public static Context getContext(){
        return mContext;
    }

    public static AppContext getInstance(){
        return mInstance;
    }

    static void initialize(Context context){
        if(mInstance == null){
            synchronized (AppContext.class){
                if(mInstance==null){
                    mInstance=new AppContext(context.getApplicationContext());
                    AppConfig.init(context);
                }
            }
        }
    }
}
