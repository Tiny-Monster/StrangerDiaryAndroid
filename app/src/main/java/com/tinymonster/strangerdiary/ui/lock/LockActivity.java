package com.tinymonster.strangerdiary.ui.lock;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.PreUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;

/**
 * Created by TinyMonster on 13/02/2019.
 */

public class LockActivity extends BaseActivity{
    private GestureLockView lock_gestureview;
    private TextView lock_text;
    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected boolean initToolbar() {
        return false;
    }

    @Override
    protected void initViews() {
        lock_gestureview=(GestureLockView)findViewById(R.id.lock_gestureview);
        lock_text=(TextView)findViewById(R.id.lock_text);
        lock_gestureview.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {
                String savedCode=(String)PreUtils.get(Const.USERINFO_KEY.GESTURE,"");
                if(result.equals(savedCode)){
                    setResult(Const.RESULT_CODE.RESULT_OK,getIntent());
                    LockActivity.this.finish();
                }else {
                    lock_text.setText("手势错误");
                    lock_text.setTextColor(Color.RED);
                    lock_gestureview.clearView();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }
}
