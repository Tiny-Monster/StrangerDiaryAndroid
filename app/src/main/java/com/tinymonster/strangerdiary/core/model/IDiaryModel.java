package com.tinymonster.strangerdiary.core.model;

import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.dao.callback.RxQueryCallback;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by TinyMonster on 19/01/2019.
 */

public interface IDiaryModel {
    Observable getDiaryFromDiskByPage(int page);

    Observable<List<DiaryBean>> getUnSynDiaryRx();

    List<DiaryBean> getUnSynDiary();
}
