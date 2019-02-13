package com.tinymonster.strangerdiary.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.tinymonster.strangerdiary.ui.lock.LockActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.PreUtils;

/**
 * Created by TinyMonster on 03/01/2019.
 */

public class MyApplication extends Application{
    private int countActivedActivity=0;
    private boolean mBackgroundEve=true;
    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig.init(this);
        AppContext.initialize(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if(countActivedActivity==0&& !PreUtils.get(Const.USERINFO_KEY.GESTURE,"").equals("")&&mBackgroundEve){
                    timeOutCheck(activity);
                }
                countActivedActivity++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                countActivedActivity--;
                if(countActivedActivity==0){
                    mBackgroundEve=true;
                    saveStartTime();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void saveStartTime(){
        PreUtils.put(Const.LOCK_TIME.START_TIME,System.currentTimeMillis());
    }

    private long getStartTime(){
        long startTime=0;
        startTime=(long)PreUtils.get(Const.LOCK_TIME.START_TIME,0L);
        return startTime;
    }

    private void timeOutCheck(Activity activity){
        long endTime=System.currentTimeMillis();
        if(endTime-getStartTime()>=Const.LOCK_TIME.TIMEOUT_ALP){
            Intent intent=new Intent(activity, LockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            activity.startActivityForResult(intent,Const.RESULT_CODE.GESTURE_LOCK);
        }
    }
}
