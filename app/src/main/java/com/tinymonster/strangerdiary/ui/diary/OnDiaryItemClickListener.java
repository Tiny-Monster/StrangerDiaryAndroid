package com.tinymonster.strangerdiary.ui.diary;

import com.tinymonster.strangerdiary.bean.DiaryBean;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public interface OnDiaryItemClickListener {

    public void onItemClick(DiaryBean bean);

    public void onItemLongClick(DiaryBean bean);

    public void onItemSynClick(DiaryBean bean);
}
