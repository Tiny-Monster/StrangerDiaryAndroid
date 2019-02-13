package com.tinymonster.strangerdiary.net.netconst;

/**
 * Api接口地址
 * 保存base 和  附加的url
 */
public class UrlContainer {
    public static final String baseUrl = "http://39.105.104.164:80/monster-diary/";
    /**
     * 登录
     */
    public static final String LOGIN = "user/login";

    /**
     * 注册
     */
    public static final String REGISTER = "user/register";

    /**
     * 注销登陆
     */
    public static final String LOGOUT="user/logout";
    /**
     * 上传图片
     */
    public static final String UPLOADPIC="uploadimages";
    /**
     * 上传日记
     */
    public static final String UPLOADDIARY="diary/updiary";
    /**
     * 下载所有日记
     */
    public static final String DOWNLOADALLDATA="diary/getAllByUserId";
    /**
     * 更新日记
     */
    public static final String UPDATEDIARY="diary/updatediary";
}
