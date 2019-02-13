package com.tinymonster.strangerdiary.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by TinyMonster on 31/12/2018.
 */

public class MonsterImageView extends android.support.v7.widget.AppCompatImageView{
    private String url;

    public MonsterImageView(Context context) {
        super(context);
    }

    public MonsterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonsterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
