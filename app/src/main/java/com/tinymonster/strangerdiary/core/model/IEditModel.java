package com.tinymonster.strangerdiary.core.model;

import android.support.v7.widget.DefaultItemAnimator;

import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.bean.ImagesUrlBean;
import com.tinymonster.strangerdiary.net.callback.RxObserver;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public interface IEditModel {

    void saveDiary( List<String> picPaths,DiaryBean diaryBean, RxObserver<DiaryBean> callback);

    void UpdatePic(List<String> picPaths,RxObserver<ImagesUrlBean> callback);

    void saveDiary(DiaryBean diaryBean, RxObserver<DiaryBean> callback);
}
