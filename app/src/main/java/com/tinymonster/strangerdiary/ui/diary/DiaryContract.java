package com.tinymonster.strangerdiary.ui.diary;

import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.core.view.IListDataView;
import com.tinymonster.strangerdiary.core.view.IView;

import java.util.List;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public interface DiaryContract {

    interface IDiaryView extends IListDataView<DiaryBean> {
        void notifyItemData(DiaryBean diaryBean) ;
    }

    interface IDiaryPresenter{

        void getDataFromDisk();

        void getDateFromNet();

        void synData();

        List<String> getPicPathList(String content);
    }
}
