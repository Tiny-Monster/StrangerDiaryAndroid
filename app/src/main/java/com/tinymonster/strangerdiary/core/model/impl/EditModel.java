package com.tinymonster.strangerdiary.core.model.impl;

import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.bean.ImagesUrlBean;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.core.model.IEditModel;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.utils.File2PartsUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class EditModel extends BaseModel implements IEditModel {

    @Override
    public void saveDiary(List<String> picPaths, final DiaryBean diaryBean, RxObserver<DiaryBean> callback) {
        doRxRequest()
                .UploadImage(File2PartsUtils.files2Parts("images", picPaths))
                .flatMap(new Function<BaseBean<ImagesUrlBean>, Observable<BaseBean<DiaryBean>>>() {
                    @Override
                    public Observable<BaseBean<DiaryBean>> apply(BaseBean<ImagesUrlBean> imagesUrlBeanBaseBean) throws Exception {
                        Map<String, String> imgPathMap = imagesUrlBeanBaseBean.data.getImages();
                        Pattern patten = Pattern.compile("img.+?pic");
                        String content = diaryBean.getContent();
                        Matcher matcher = patten.matcher(content);
                        while (matcher.find()) {
                            String path = matcher.group().toString();
                            path = path.substring(3, path.length() - 3);
                            String[] splitPath = path.split("/");
                            if (imgPathMap.containsKey(splitPath[splitPath.length - 1])) {
                                content = content.replace(path, imgPathMap.get(splitPath[splitPath.length - 1]));
                            }
                        }
                        diaryBean.setContent(content);
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(diaryBean);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonData);
                        if(diaryBean.getId()==0){
                            return doRxRequest().UploadDiary(body);
                        }else {
                            return doRxRequest().UpdateDiary(body);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    @Override
    public void UpdatePic(List<String> picPaths, RxObserver<ImagesUrlBean> callback) {
        doRxRequest()
                .UploadImage(File2PartsUtils.files2Parts("images", picPaths))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    @Override
    public void saveDiary(DiaryBean diaryBean, RxObserver<DiaryBean> callback) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(diaryBean);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonData);
        if(diaryBean.getId()!=null&&diaryBean.getId()!=0){
            doRxRequest().UpdateDiary(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
        }else {
            doRxRequest().UploadDiary(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
        }
    }
}
