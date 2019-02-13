package com.tinymonster.strangerdiary.ui.register;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.core.model.impl.RegisterModel;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.SMSCallback;
import com.tinymonster.strangerdiary.net.callback.SMSEventHandler;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by TinyMonster on 11/01/2019.
 *
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.IRegisterView> implements RegisterContract.IRegisterPresenter,SMSCallback{
    RegisterModel registerModel;
    RegisterContract.IRegisterView registerView;
    SMSEventHandler smsEventHandler;

    public RegisterPresenter(){
        this.registerModel=new RegisterModel();
    }

    @Override
    public void attachView(RegisterContract.IRegisterView view) {
        super.attachView(view);
        smsEventHandler=new SMSEventHandler(this);
        SMSSDK.registerEventHandler(smsEventHandler);
    }

    @Override
    public void getVerifyCode() {
        if(verifyAccount()){
            registerModel.getVerificationCode("86",registerView.getAccount());
        }
    }

    @Override
    public void register() {
        if(verifyUserInfo()){
            registerModel.submitVerificationCode("86",registerView.getAccount(),registerView.getVerifyCode());
        }
    }

    /**
     * 验证用户账号是否是有效的手机号
     * @return
     */
    private boolean verifyAccount() {
        registerView = getView();
        return registerModel.verifyAccount(registerView.getAccount(), verifyAccountCallback);
    }

    /**
     * 验证用户注册信息是否合法
     * @return
     */
    private boolean verifyUserInfo() {
        registerView = getView();
        return registerModel.verifyUserInfo(registerView.getAccount(), registerView.getPassword(),registerView.getPswConfirm(),verifyAccountCallback);
    }

    /**
     * UI显示信息接口
     */
    private VerifyAccountCallback verifyAccountCallback=new VerifyAccountCallback() {
        @Override
        public void onVerifyResult(String msg) {
            registerView.showResult(msg);
        }
    };

    /**
     * 获取短信成功回调接口
     * @param data
     */
    @Override
    public void onGetSMSSuccess(Object data) {
        verifyAccountCallback.onVerifyResult(AppContext.getContext().getString(R.string.sms_send_success));
    }

    /**
     * 获取短信失败回调接口
     * @param data
     */
    @Override
    public void onGetSMSFail(Object data) {
        verifyAccountCallback.onVerifyResult(AppContext.getContext().getString(R.string.sms_send_fail));
    }

    /**
     * 验证短信成功回调接口
     * @param data
     */
    @Override
    public void onVerifySMSSuccess(Object data) {
        RxObserver rxObserver=new RxObserver(this) {
            @Override
            protected void onSuccess(BaseBean data) {
                registerView.onRegisterSuccess();
            }

            @Override
            protected void onFail(String errorMsg) {
                registerView.onRegisterFail(errorMsg);
            }
        };
        registerModel.register(registerView.getAccount(),registerView.getPassword(),rxObserver);
        addDisposable(rxObserver);
    }

    /**
     * 验证短信失败回调接口
     * @param data
     */
    @Override
    public void onVerifySMSFail(Object data) {
        verifyAccountCallback.onVerifyResult(AppContext.getContext().getString(R.string.sms_verify_fail));
    }

    @Override
    public void detachView() {
        SMSSDK.unregisterEventHandler(smsEventHandler);
        super.detachView();
    }
}
