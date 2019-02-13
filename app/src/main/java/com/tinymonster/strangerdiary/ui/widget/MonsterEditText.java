package com.tinymonster.strangerdiary.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Created by TinyMonster on 31/12/2018.
 */

public class MonsterEditText extends android.support.v7.widget.AppCompatEditText{

    public MonsterEditText(Context context) {
        super(context);
    }

    public MonsterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonsterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new DeleteInputConnection(super.onCreateInputConnection(outAttrs),true);
    }

    /**
     * 自定义InputConnectionWrapper
     * 在deleteSurroundingText中调用sendKeyEvent，避免某些输入法点击删除按键时不调用sendKeyEvent，导致不能删除
     */
    private class DeleteInputConnection extends InputConnectionWrapper{

        public DeleteInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if(beforeLength==1&&afterLength==0){
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        &&sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

}
