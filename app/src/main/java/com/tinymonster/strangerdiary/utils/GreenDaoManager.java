package com.tinymonster.strangerdiary.utils;

import android.content.Context;

import com.tinymonster.strangerdiary.dao.DaoMaster;
import com.tinymonster.strangerdiary.dao.DaoSession;

import org.greenrobot.greendao.async.AsyncSession;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class GreenDaoManager {
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static volatile GreenDaoManager mInstance = null;
    private static final String DB_NAME = "GreenDao.db";
    private static AsyncSession asyncSession=null;

    private GreenDaoManager() {
    }

    public static void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new
                DaoMaster.DevOpenHelper(context, DB_NAME); //
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());//数据库对象，用于创建表和删除表
        mDaoSession = mDaoMaster.newSession();  //管理dao对象，DAO对象中存在这增删改查等API
        asyncSession= mDaoSession.startAsyncSession();
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public AsyncSession getAsyncSession(){
        return asyncSession;
    }
}
