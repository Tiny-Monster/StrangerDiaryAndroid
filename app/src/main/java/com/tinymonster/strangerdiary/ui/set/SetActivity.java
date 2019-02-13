package com.tinymonster.strangerdiary.ui.set;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.PreUtils;

/**
 * Created by TinyMonster on 12/02/2019.
 */

public class SetActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    private TextView set_change_pwd;
    private TextView set_gesture;
    private Switch set_gesture_switch;
    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected boolean initToolbar() {
        mToolbar.setTitle("设置");
        return true;
    }

    @Override
    protected void initViews() {
        set_change_pwd=(TextView)findViewById(R.id.set_change_pwd);
        set_gesture=(TextView)findViewById(R.id.set_gesture);
        set_gesture_switch=(Switch)findViewById(R.id.set_gesture_switch);
        String gesture=(String) PreUtils.get(Const.USERINFO_KEY.GESTURE,"");
        if(gesture==null||gesture.equals("")){
            set_gesture_switch.setChecked(false);
        }else {
            set_gesture_switch.setChecked(true);
        }
        set_gesture_switch.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.set_gesture_switch:
                if(b){
                    PreUtils.remove(Const.USERINFO_KEY.GESTURE);
                    Intent gesture_intent=new Intent(SetActivity.this,GestureActivity.class);
                    startActivity(gesture_intent);
                    finish();
                }else {
                    PreUtils.remove(Const.USERINFO_KEY.GESTURE);
                }
                break;
            default:
                break;
        }
    }
}
