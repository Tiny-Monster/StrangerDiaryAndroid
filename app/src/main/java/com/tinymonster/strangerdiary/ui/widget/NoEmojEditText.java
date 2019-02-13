package com.tinymonster.strangerdiary.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.tinymonster.strangerdiary.utils.StringUtils;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public class NoEmojEditText extends AppCompatEditText{
    public NoEmojEditText(Context context) {
        super(context);
        this.setFilters(new InputFilter[]{StringUtils.passwordFilter(context)});
    }

    public NoEmojEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFilters(new InputFilter[]{StringUtils.passwordFilter(context)});
    }

    public NoEmojEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFilters(new InputFilter[]{StringUtils.passwordFilter(context)});
    }


}
