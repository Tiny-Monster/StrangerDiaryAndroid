package com.tinymonster.strangerdiary.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.event.EventBus;
import com.tinymonster.strangerdiary.ui.lock.LockActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LightStatusBarUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected Toolbar mToolbar;
    protected FrameLayout mContainer;
    protected ProgressDialog loadingDialog=null;
    private PublishSubject mSubject;
    private DisposableObserver mDisposableObserver;
    private EventBus eventBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            String FRAGMENTS_TAG="android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Intent intent=getIntent();
        if(intent!=null)
            dealIntent(intent);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContainer=(FrameLayout)findViewById(R.id.frameLayout);
        boolean isToolbar = initToolbar();
        if (isToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                //必须要在setSupportActionBar之后,不然不起作用
                @Override
                public void onClick(View v) {
                    onNavigationClick();
                }
            });
        } else {
            mToolbar.setVisibility(View.GONE);
        }
        initContent(getLayoutId());
        eventBus=EventBus.getInstance();
        mSubject=eventBus.registerEvent(registerEvent());
        mDisposableObserver=new ReceiveEvent();
        mSubject.subscribe(mDisposableObserver);
    }

    protected abstract void dealIntent(Intent intent);

    protected abstract boolean initToolbar();

    protected abstract void initViews();

    protected void onNavigationClick() {
        finish();
    }

    /**
     * 设置content的LayoutId
     * @param layoutId
     */
    private void initContent(int layoutId) {
        if (layoutId != 0) {
            View contentView = LayoutInflater.from(this).inflate(layoutId, mContainer, false);
            mContainer.addView(contentView);
            initViews();
        }
    }

    protected abstract int getLayoutId();

    protected abstract String registerEvent();

    protected abstract void receiveEvent(Object object);

    private class ReceiveEvent extends DisposableObserver {
        @Override
        public void onNext(Object o) {
            receiveEvent(o);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    }

    /**
     * 创建Dialog
     */
    private void createLoadingDialog(){
        if(loadingDialog==null){
            loadingDialog=new ProgressDialog(this);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 显示dialog
     * @param title
     */
    protected void showLoadingDialog(String title){
        createLoadingDialog();
        loadingDialog.setMessage(title);
        if(!loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }
    /**
     * 显示dialog
     */
    protected void showLoadingDialog(){
        createLoadingDialog();
        if(!loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }

    /**
     * 隐藏loading
     */
    protected void hideLoadingDialog(){
        if(loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unRegisterEvent(registerEvent(),mSubject,mDisposableObserver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Const.RESULT_CODE.GESTURE_LOCK:
                LogUtils.d("收到解锁回复,resultCode="+resultCode);
//                if(resultCode==Const.RESULT_CODE.RESULT_OK){
//                    ToastUtils.showToast(AppContext.getContext(),"解锁成功");
//                }else {
//                    LogUtils.d("finish");
//                    finish();
//                }
        }
    }
}
