package com.tinymonster.strangerdiary.ui.diary;

import android.content.Intent;

import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.core.model.impl.DiaryModel;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.dao.callback.RxQueryCallback;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.ui.login.LoginActivity;
import com.tinymonster.strangerdiary.ui.main.MainActivity;
import com.tinymonster.strangerdiary.utils.DaoUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class DiaryPresenter extends BasePresenter<DiaryContract.IDiaryView> implements DiaryContract.IDiaryPresenter{
    DiaryModel diaryModel;

    public DiaryPresenter(){
        diaryModel=new DiaryModel();
    }

    @Override
    public void getDataFromDisk() {
        if(UserInfoManager.isLogin()){
            diaryModel.getDiaryFromDiskByPage(getView().getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<DiaryBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getView().showLoading("");
                        }

                        @Override
                        public void onNext(List<DiaryBean> diaryBeans) {
                            if(getView().getPage()==0){
                                getView().clearListData();
                            }
                            getView().getData().addAll(diaryBeans);
                            if(getView().getData().size()==0){
                                getView().showEmpty();
                            }else {
                                getView().showContent();
                                getView().autoLoadMore();
                            }
                            if(diaryBeans.size()==0){
                                getView().showNoMore();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideLoading();
                            getView().showFail(e.getMessage());
                            getView().showNoMore();
                        }

                        @Override
                        public void onComplete() {
                            getView().hideLoading();
                        }
                    });
        }else {
            getView().showEmpty();
        }
    }

    @Override
    public void getDateFromNet() {
        LogUtils.d("从网络加载数据");
    }

    @Override
    public void synData() {
        if(UserInfoManager.isLogin()){
            final List<DiaryBean> DiskDiaryBeans=diaryModel.getUnSynDiary();
            final int length=DiskDiaryBeans.size();
            LogUtils.d("length:"+length);
            if(DiskDiaryBeans.size()>0){
                Observable.intervalRange(0,length,0,1,TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(final Long aLong) throws Exception {
                                LogUtils.d("收到数字："+aLong);
                                final String content=DiskDiaryBeans.get(aLong.intValue()).getContent();
                                List<String> imagePath=getPicPathList(DiskDiaryBeans.get(aLong.intValue()).getContent());
                                if(imagePath.size()>0){
                                    diaryModel.saveDiary(getPicPathList(DiskDiaryBeans.get(aLong.intValue()).getContent()), DiskDiaryBeans.get(aLong.intValue()), new RxObserver<DiaryBean>(DiaryPresenter.this) {
                                        @Override
                                        protected void onSuccess(BaseBean<DiaryBean> data) {
                                            LogUtils.d("成功同步一个");
                                            DiaryBean diaryBean=DiskDiaryBeans.get(aLong.intValue());
                                            diaryBean.setIsSyn("1");
                                            if(diaryBean.getId()==0){
                                                diaryBean.setId(data.data.getId());
                                            }
                                            diaryBean.setContent(content);
                                            DaoUtils.DiaryDaoUtils.updateDiary(diaryBean);
                                            getView().notifyItemData(diaryBean);
                                            if(aLong.intValue()==length-1){
                                                getView().hideLoading();
                                            }
                                        }

                                        @Override
                                        protected void onFail(String errorMsg) {
                                            LogUtils.d("第"+aLong+"个日记同步错误");
                                        }
                                    });
                                }else {
                                    diaryModel.saveDiary(DiskDiaryBeans.get(aLong.intValue()), new RxObserver<DiaryBean>(DiaryPresenter.this) {
                                        @Override
                                        protected void onSuccess(BaseBean<DiaryBean> data) {
                                            LogUtils.d("成功同步一个");
                                            DiaryBean diaryBean=DiskDiaryBeans.get(aLong.intValue());
                                            diaryBean.setIsSyn("1");
                                            diaryBean.setContent(content);
                                            if(diaryBean.getId()==0){
                                                diaryBean.setId(data.data.getId());
                                            }
                                            DaoUtils.DiaryDaoUtils.updateDiary(diaryBean);
                                            getView().notifyItemData(diaryBean);
                                            if(aLong.intValue()==length-1){
                                                getView().hideLoading();
                                            }
                                        }

                                        @Override
                                        protected void onFail(String errorMsg) {
                                            LogUtils.d("第"+aLong+"个日记同步错误");
                                        }
                                    });
                                }
                            }
                        });
            }else {
                getView().showFail("您的所有日记已同步到云端");
                getView().hideLoading();
            }
        }
    }

    @Override
    public List<String> getPicPathList(String content) {
        List<String> PicPaths=new ArrayList<>();
        Pattern patten= Pattern.compile("img.+?pic");
        Matcher matcher = patten.matcher(content);
        while(matcher.find()){
            String path=matcher.group().toString();
            LogUtils.d("path:"+path);
            path=path.substring(3,path.length()-3);
            if(!path.startsWith("/monster")){
                PicPaths.add(path);
            }
        }
        return PicPaths;
    }

}
