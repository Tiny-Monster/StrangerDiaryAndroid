package com.tinymonster.strangerdiary.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.tinymonster.strangerdiary.R;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public class TimeCount extends CountDownTimer{

    private Button button;

    //参数依次为总时长,和计时的时间间隔
    public TimeCount(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    //计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        String time = "(" + millisUntilFinished / 1000 + ")秒";
        setButtonInfo(time, "#c1c1c1", false);
    }

    //计时完毕时触发
    @Override
    public void onFinish() {
        setButtonInfo("重新获取", "#d7d7d7", true);
    }

    /**
     * 验证按钮在点击前后相关设置
     *
     * @param content 要显示的内容
     * @param color  颜色值
     * @param isClick 是否可点击
     */
    private void setButtonInfo(String content, String color, boolean isClick) {

        button.setText(content);
        if(isClick) //可以单击
        {
            button.setBackgroundResource(R.drawable.message_send_button);//gxw+
        }
        else { //不可单击
            button.setBackgroundColor(Color.parseColor(color));
        }
        button.setClickable(isClick);
    }
}
