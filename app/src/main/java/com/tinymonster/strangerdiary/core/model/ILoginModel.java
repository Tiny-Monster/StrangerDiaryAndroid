package com.tinymonster.strangerdiary.core.model;

import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public interface ILoginModel {

    /**
     * 登陆
     * @param account
     * @param password
     * @param callback
     */
    void login(String account,String password,RxObserver<UserBean> callback);
    /**
     * 保存用户信息
     * @param userBean
     */
    void saveUserInfo(UserBean userBean);
    /**
     * 检验帐号
     * @param username
     * @param password
     * @return
     */
    boolean verifyAccount(String username, String password, VerifyAccountCallback callback);

    /**
     * 保存token
     * @param token
     */
    void saveToken(String token);
}
