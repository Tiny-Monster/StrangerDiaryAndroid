package com.tinymonster.strangerdiary.ui.edit;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.bean.ImagesUrlBean;
import com.tinymonster.strangerdiary.core.model.impl.EditModel;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.net.RxRetrofit;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.ui.base.BasePresenterActivity;
import com.tinymonster.strangerdiary.utils.DaoUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by TinyMonster on 16/01/2019.
 */

public class EditPresenter extends BasePresenter<EditContract.IEditView> implements EditContract.IEditPresenter {
    private EditModel editModel;

    public EditPresenter() {
        this.editModel = new EditModel();
    }

    @Override
    public void saveDiary() {
        final DiaryBean diaryBean = getView().getEditor().BuildEditData();
        if(diaryBean.getContent()!=null&&!diaryBean.getContent().isEmpty()){
            diaryBean.setIsSyn("1");
            List<String> paths = getPicPathList(diaryBean.getContent());
            if (paths.size() > 0) {
                RxObserver<DiaryBean> observer = new RxObserver<DiaryBean>(this) {
                    @Override
                    protected void onSuccess(BaseBean<DiaryBean> data) {
                        //TODO 上传成功
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("1");
                        saveBean.setId(data.data.getId());
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                    }

                    @Override
                    protected void onFail(String errorMsg) {
                        //TODO 上传失败 isSyn=false 保存到本地
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("0");
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("0");
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                        getView().onSaveDiarySuccess();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        getView().onSaveDiarySuccess();
                    }
                };
                editModel.saveDiary(paths, diaryBean, observer);
            } else {
                editModel.saveDiary(diaryBean, new RxObserver<DiaryBean>(EditPresenter.this) {
                    @Override
                    protected void onSuccess(BaseBean<DiaryBean> data) {
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("1");
                        saveBean.setId(data.data.getId());
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                    }

                    @Override
                    protected void onFail(String errorMsg) {
                        //TODO 上传失败 isSyn=false 保存到本地
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("0");
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DiaryBean saveBean = getView().getEditor().BuildEditData();
                        saveBean.setIsSyn("0");
                        saveBean.setDate(diaryBean.getDate());
                        if (DaoUtils.DiaryDaoUtils.insertDiary(saveBean)) {
                            getView().postEvent(saveBean);
                        } else {
                            getView().showError("数据库错误");
                        }
                        getView().onSaveDiarySuccess();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        getView().onSaveDiarySuccess();
                    }
                });
            }
        }else {
            getView().showError("您的日记内容不能为空");
        }

    }

    @Override
    public String getPhotoFileName() {
        String str = "DIARY_IMG_" + UserInfoManager.getUserId() + "_" + System.currentTimeMillis();
        return str + ".jpg";
    }

    @Override
    public Intent getTakePickIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    @Override
    public void insertBitmap(String path) {
        getView().getEditor().insertImage(path);
    }

    @Override
    public List<String> getPicPathList(String content) {
        List<String> PicPaths = new ArrayList<>();
        Pattern patten = Pattern.compile("img.+?pic");
        Matcher matcher = patten.matcher(content);
        while (matcher.find()) {
            String path = matcher.group().toString();
            path = path.substring(3, path.length() - 3);
            if (!path.startsWith("/monster"))
                PicPaths.add(path);
        }
        return PicPaths;
    }


}
