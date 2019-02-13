package com.tinymonster.strangerdiary.net.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 短信验证回调类
 * Created by TinyMonster on 11/01/2019.
 */

public class SMSEventHandler extends EventHandler{
    private SMSCallback smsCallback;

    public SMSEventHandler(SMSCallback smsCallback){
        this.smsCallback=smsCallback;
    }

    @Override
    public void afterEvent(int i, int i1, Object o) {
        Message message=new Message();
        message.arg1=i;
        message.arg2=i1;
        message.obj=o;
        new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int event = message.arg1;
                int result = message.arg2;
                Object data = message.obj;
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        smsCallback.onGetSMSSuccess(data);
                    } else {
                        // TODO 处理错误的结果
                        ((Throwable) data).printStackTrace();
                        smsCallback.onGetSMSFail(data);
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        smsCallback.onVerifySMSSuccess(data);
                    } else {
                        // TODO 处理错误的结果
                        ((Throwable) data).printStackTrace();
                        smsCallback.onVerifySMSFail(data);
                    }
                }
                // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                return false;
            }
        }).sendMessage(message);
    }

}
