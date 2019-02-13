package com.tinymonster.strangerdiary.core.model;

import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;
import com.tinymonster.strangerdiary.utils.StringUtils;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public interface IRegisterModel {
    /**
     * 发送验证码
     * @param country
     * @param phone
     * @param code
     */
    void submitVerificationCode(String country,String phone,String code);
    /**
     * 获取验证短信
     * @param country
     * @param phone
     */
    void getVerificationCode(String country,String phone);

    /**
     * 检验帐号
     * @param username
     * @return
     */
    boolean verifyAccount(String username, VerifyAccountCallback callback);

    /**
     * 验证用户所填注册信息是否正确
     * @param username
     * @param password
     * @param confirmPwd
     * @param callback
     * @return
     */
    boolean verifyUserInfo(String username,String password,String confirmPwd, VerifyAccountCallback callback);

    /**
     * 登陆
     * @param account
     * @param password
     * @param callback
     */
    void register(String account,String password,RxObserver<UserBean> callback);
}
