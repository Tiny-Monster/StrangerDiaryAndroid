package com.tinymonster.strangerdiary.net.callback;

import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.net.netconst.NetConfig;

/**
 * 实现了onNext接口,向外提供了请求成功回调和请求失败回调
 * Created by TinyMonster on 10/01/2019.
 */

public abstract class RxObserver<T> extends RxBaseObserver<T>{

     public RxObserver(BasePresenter presenter) {
        super(presenter);
    }

    @Override
    public void onNext(BaseBean<T> tBaseBean) {
        if(tBaseBean.success== NetConfig.REQUEST_SUCCESS){
            onSuccess(tBaseBean);
        }else {
            onFail(tBaseBean.error);
        }
    }

    /**
     * 网络请求成功的回调
     * @param data
     */
    protected abstract void onSuccess(BaseBean<T> data);

    /**
     * 网络请求失败的回调
     * @param errorMsg
     */
    protected abstract void onFail(String errorMsg);
}
