package com.tinymonster.strangerdiary.core.model.impl;

import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.core.model.IDiaryModel;
import com.tinymonster.strangerdiary.dao.callback.RxQueryCallback;
import com.tinymonster.strangerdiary.utils.DaoUtils;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by TinyMonster on 19/01/2019.
 */

public class DiaryModel extends EditModel implements IDiaryModel{
    @Override
    public Observable<List<DiaryBean>> getDiaryFromDiskByPage(int page) {
//        DaoUtils.DiaryDaoUtils.getDiaryFromDiskByPageRx(page,callback);
        return DaoUtils.DiaryDaoUtils.getDiaryFromDiskByPageRx2(page);
    }

    @Override
    public Observable<List<DiaryBean>> getUnSynDiaryRx(){
        return DaoUtils.DiaryDaoUtils.getUnSynDiaryRx();
    }

    @Override
    public List<DiaryBean> getUnSynDiary() {
        return DaoUtils.DiaryDaoUtils.getUnSynDiary();
    }
}
