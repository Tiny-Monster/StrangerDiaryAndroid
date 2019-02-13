package com.tinymonster.strangerdiary.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public class Const {
    //事件Action
    public static class EVENT_ACTION {
        public static final String REFRESH_DATA = "refresh_list_item";
    }

    public static class TOAST_MSG{
        public static final String NET_ERROR = "网络错误";
        public static final String UNKNOWN_ERROR = "未知错误";
    }

    public static class LIST_VIEW_TYPE{
        public static final int VIEW_TYPE_NOMAL = 0;//nomal
        public static final int VIEW_TYPE_HEADER = 100;//header
        public static final int VIEW_TYPE_FOOTER = 200;//footer
    }

    public static class TIME_FORMAT{
        public static final String DATA_FORMAT="yyyy-MM-dd HH:mm:ss";
        public static final String DAY_FORMAT="yyyy-MM-dd";
        public static final String TIME_FORMAT="HH:mm:ss";
    }

    //当前页面状态
    public static class PAGE_STATE {
        public static final int STATE_REFRESH = 0; //刷新
        public static final int STATE_LOAD_MORE = 1;//加载更多
    }
    //用户相关
    public static class USERINFO_KEY {
        public static final String USER_INFO = "mUserInfo";  //用户信息
        public static final String IS_LOGIN = "mIsLogin";    //登录状态
        public static final String AES = "mAES";//用户信息密钥
        public static final String USER_ID="mUserId";
        public static final String GESTURE="gesture";
    }

    public static class INTENT_TAG{
        public static final String setFrom="setFrom";
        public static final String getFrom="setFrom";
    }

    public static class FILE_PATH{
        public static final File PHOTO_DIR = new File(
                Environment.getExternalStorageDirectory() + "/DCIM/diary");
    }

    public static class EDIT_ACTIVITY{
        public static final int CALL_FOR_SELECT_PIC=1;
        public static final int CALL_FOR_PAINT=2;
        public static final int CALL_FOR_TAKE_PIC=3;
    }

    //Intent传值
    public static class BUNDLE_KEY {
        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String OBJ = "obj";
        public static final String SAVE_TYPE="SAVE_TYPE";// 0：磁盘  1：在线
    }

    public static class LOCK_TIME{
        public static final String START_TIME="START_TIME";
        public static final int TIMEOUT_ALP=60;
    }

    public static class RESULT_CODE{
        public static final int GESTURE_LOCK=9009;
        public static final int RESULT_OK=1;
    }
}
