package com.tinymonster.strangerdiary.ui.set;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TextView;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.ui.main.MainActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.PreUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;

/**
 * Created by TinyMonster on 13/02/2019.
 */

public class GestureActivity extends BaseActivity{
    private GestureLockView gesture_view;
    private TextView gesture_text_1;
    private TextView gesture_text_2;
    private int state=0;
    private String firstCode="";
    private String secondCode="";
    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected boolean initToolbar() {
        return true;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle("请输入手势解锁密码");
        gesture_view=(GestureLockView)findViewById(R.id.gesture_view);
        gesture_text_1=(TextView)findViewById(R.id.gesture_text_1);
        gesture_text_2=(TextView)findViewById(R.id.gesture_text_2);
        gesture_view.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {
                if(state==0){
                    if(result!=null&&!result.equals("")){
                        firstCode=result;
                    }else {
                        ToastUtils.showToast(AppContext.getContext(),"您输入的手势不符合规范，请重新输入");
                        state=0;
                        gesture_view.clearView();
                    }
                }else {
                    if(result!=null&&!result.equals("")){
                        secondCode=result;
                    }else {
                        ToastUtils.showToast(AppContext.getContext(),"您输入的手势不符合规范，请重新输入");
                        gesture_view.clearView();
                    }
                }
            }
        });
        gesture_text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gesture_view.clearView();
                if(state==0){
                    firstCode="";
                    secondCode="";
                }else {
                    secondCode="";
                }
            }
        });
        gesture_text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state==0){
                    state=1;
                    gesture_view.clearView();
                    mToolbar.setTitle("请确认手势解锁密码");
                }else {
                    if(firstCode.equals(secondCode)){
                        PreUtils.put(Const.USERINFO_KEY.GESTURE,secondCode);
                        Intent intent=new Intent(GestureActivity.this, SetActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        ToastUtils.showToast(AppContext.getContext(),"两次输入不一致，请重新输入");
                        mToolbar.setTitle("请输入手势解锁密码");
                        state=0;
                        firstCode="";
                        secondCode="";
                        gesture_view.clearView();
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gesture;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }
}
