package com.tinymonster.strangerdiary.net.callback;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public interface SMSCallback {
    /**
     * 获取短信成功回调接口
     * @param data
     */
    void onGetSMSSuccess(Object data);

    /**
     * 获取短信失败回调接口
     * @param data
     */
    void onGetSMSFail(Object data);

    /**
     * 验证短信成功回调接口
     * @param data
     */
    void onVerifySMSSuccess(Object data);

    /**
     * 验证短信失败回调接口
     * @param data
     */
    void onVerifySMSFail(Object data);
}
