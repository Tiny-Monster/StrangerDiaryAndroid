package com.tinymonster.strangerdiary.application;

import android.content.Context;

import com.mob.MobSDK;
import com.tinymonster.strangerdiary.net.RxRetrofit;
import com.tinymonster.strangerdiary.net.netconst.UrlContainer;
import com.tinymonster.strangerdiary.utils.GreenDaoManager;
import com.tinymonster.strangerdiary.utils.PreUtils;

/**
 * Created by TinyMonster on 03/01/2019.
 */

public class AppConfig {
    /**
     * 初始化操作
     * @param context
     */
    static void init(Context context){
        MobSDK.init(context);
        RxRetrofit.getInstance().initRxRetrofit(context, UrlContainer.baseUrl);
        PreUtils.init(context);
        GreenDaoManager.init(context);
    }
}
