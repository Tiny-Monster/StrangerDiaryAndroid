package com.tinymonster.strangerdiary.utils;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.tinymonster.strangerdiary.bean.UserBean;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class UserInfoManager {
    public static void saveUserInfo(UserBean userBean){
        saveUserId(userBean.getId());
        //获取用户信息
        String userInfo = new Gson().toJson(userBean);
        //创建密匙
        SecretKeySpec keySpec=AesEncryptionUtils.createKey();
        //加密数据
        String aesContent=AesEncryptionUtils.encrypt(keySpec,userInfo);
        //保存数据
        PreUtils.put(Const.USERINFO_KEY.USER_INFO,aesContent);
        //保存密匙
        saveAesKey(keySpec);
    }

    /**
     * 清除用户数据
     */
    public static void removeUserInfo(){
        PreUtils.remove(Const.USERINFO_KEY.USER_INFO);
        PreUtils.remove(Const.USERINFO_KEY.GESTURE);
    }

    public static UserBean getUserInfo(){
        UserBean userBean=null;
        SecretKeySpec keySpec=getAesKey();//获得密匙
        String userInfo = AesEncryptionUtils.decrypt(keySpec,(String)PreUtils.get(Const.USERINFO_KEY.USER_INFO,""));
        if(StringUtils.isEmpty(userInfo)) return null;
        userBean=new Gson().fromJson(userInfo,UserBean.class);
        return userBean;
    }
    /**
     * 获得登陆状态
     * @return
     */
    public static boolean isLogin() {
        return (boolean) PreUtils.get(Const.USERINFO_KEY.IS_LOGIN, false);
    }
    /**
     * 保存登陆状态
     * @param isLogin
     */
    public static void saveIsLogin(boolean isLogin){
        PreUtils.put(Const.USERINFO_KEY.IS_LOGIN,isLogin);
    }

    /**
     * 保存密匙
     * @param keySpec
     */
    public static void saveAesKey(SecretKeySpec keySpec){
        PreUtils.put(Const.USERINFO_KEY.AES, Base64.encodeToString(keySpec.getEncoded(),Base64.DEFAULT));
    }
    /**
     * 获得密匙
     * @return
     */
    public static SecretKeySpec getAesKey(){
        String keyStr = (String) PreUtils.get(Const.USERINFO_KEY.AES, "");
        return AesEncryptionUtils.getSecretKey(Base64.decode(keyStr, Base64.DEFAULT));
    }

    public static void saveUserId(Long id){
        PreUtils.put(Const.USERINFO_KEY.USER_ID,id);
    }

    public static Long getUserId(){
        Long userId = (Long) PreUtils.get(Const.USERINFO_KEY.USER_ID,0L);
        return userId;
    }
}
