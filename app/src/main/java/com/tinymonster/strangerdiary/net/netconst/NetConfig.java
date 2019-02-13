package com.tinymonster.strangerdiary.net.netconst;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public interface NetConfig {
    boolean REQUEST_SUCCESS = true; //请求成功
    boolean REQUEST_ERROR = false;  //请求失败
    /**
     * 连接错误,网络异常
     */
    int CONNECT_ERROR = 1001;
    /**
     * 连接超时
     */
    int CONNECT_TIMEOUT = 1002;

    /**
     * 解析错误
     */
    int PARSE_ERROR = 1003;
    /**
     * 未知错误
     */
    int UNKNOWN_ERROR = 1004;

    /**
     * 请求超时
     */
    int REQUEST_TIMEOUT = 1005;



}
