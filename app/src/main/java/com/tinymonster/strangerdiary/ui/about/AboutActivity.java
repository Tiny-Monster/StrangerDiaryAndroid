package com.tinymonster.strangerdiary.ui.about;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;

/**
 * Created by TinyMonster on 23/01/2019.
 */

public class AboutActivity extends BaseActivity{
    private TextView about_introduce;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(R.string.about_us);
    }

    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected boolean initToolbar() {
        return true;
    }

    @Override
    protected void initViews() {
        about_introduce=(TextView)findViewById(R.id.about_introduce);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            about_introduce.setText(Html.fromHtml(getString(R.string.about_introduce), Html.FROM_HTML_MODE_LEGACY));
        } else {
            about_introduce.setText(Html.fromHtml(getString(R.string.about_introduce)));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }
}
