package com.tinymonster.strangerdiary.utils;


import android.os.Handler;
import android.os.Looper;

import com.mob.wrappers.UMSSDKWrapper;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.dao.DiaryBeanDao;
import com.tinymonster.strangerdiary.dao.callback.RxQueryCallback;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by TinyMonster on 19/01/2019.
 */

public class DaoUtils {

    public static class DiaryDaoUtils {
        /**
         * 保存日记
         *
         * @param diaryBean
         */
        public static boolean insertDiary(DiaryBean diaryBean) {
            return GreenDaoManager.getInstance().getSession().getDiaryBeanDao().insert(diaryBean) != -1;
        }

        /**
         * 根据id删除日记
         *
         * @param id
         */
        public void deleteDiaryById(Long id) {
            GreenDaoManager.getInstance().getSession().getDiaryBeanDao().deleteByKey(id);
        }

        /**
         * 根据bean更新bean
         *
         * @param diaryBean
         */
        public static void updateDiary(DiaryBean diaryBean) {
            GreenDaoManager.getInstance().getSession().getDiaryBeanDao().update(diaryBean);
        }

        /**
         * 异步查询所有日记
         *
         * @param callback
         */
        public static void getAllDiaryRx(final RxQueryCallback<DiaryBean> callback) {
            GreenDaoManager.getInstance().getSession().startAsyncSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    List<DiaryBean> res = GreenDaoManager.getInstance().getSession().getDiaryBeanDao().loadAll();
                    if (res == null) {
                        callback.onDaoFail("日记查询错误");
                    } else {
                        callback.onDaoSuccess(res);
                    }
                }
            });
        }

        /**
         * 获取所有的diary
         *
         * @return
         */
        public static List<DiaryBean> getAllDiary() {
            return GreenDaoManager.getInstance().getSession().getDiaryBeanDao().loadAll();
        }

        /**
         * 获取所有未同步的日记
         */
        public static List<DiaryBean> getUnSynDiary() {
            return GreenDaoManager.getInstance().getSession().getDiaryBeanDao().queryBuilder().where(DiaryBeanDao.Properties.IsSyn.eq("0")).list();
        }

        /**
         * 异步查询所有没有同步的日记
         *
         */
        public static Observable getUnSynDiaryRx() {
            return Observable.create(new ObservableOnSubscribe<List<DiaryBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<DiaryBean>> e) throws Exception {
                    final List<DiaryBean> res = GreenDaoManager.getInstance().getSession().getDiaryBeanDao().queryBuilder().orderDesc(DiaryBeanDao.Properties.Date).where(DiaryBeanDao.Properties.IsSyn.eq("0")).list();
                    if(res!=null){
                        e.onNext(res);
                    }else {
                        e.onError(new Throwable("未查询到数据"));
                    }
                    e.onComplete();
                }
            });
        }

        /**
         * @param page
         * @return
         */
        public static List<DiaryBean> getDiaryFromDiskByPage(int page) {
            return GreenDaoManager.getInstance().getSession().getDiaryBeanDao().queryBuilder().offset(page * 20).limit(20).list();
        }


        /**
         * 根据页码查询数据
         * @param page
         * @return
         */
        public static Observable getDiaryFromDiskByPageRx2(final int page) {
            return Observable.create(new ObservableOnSubscribe<List<DiaryBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<DiaryBean>> e) throws Exception {
                    LogUtils.d("DaoUtils,page="+page);
                    Thread.sleep(200);
                    final List<DiaryBean> res = GreenDaoManager.getInstance().getSession().getDiaryBeanDao().queryBuilder().orderDesc(DiaryBeanDao.Properties.Date).offset(page * 20).where(DiaryBeanDao.Properties.UserId.eq(UserInfoManager.getUserId())).limit(20).list();
                    if(res!=null){
                        e.onNext(res);
                    }else {
                        e.onError(new Throwable("未查询到数据"));
                    }
                    e.onComplete();
                }
            });
        }

        /**
         * 删除该用户所有日记
         */
        public static void clearAllDiary(){
            GreenDaoManager.getInstance().getSession().getDiaryBeanDao().queryBuilder().where(DiaryBeanDao.Properties.UserId.eq(UserInfoManager.getUserId())).buildDelete().executeDeleteWithoutDetachingEntities();
//            GreenDaoManager.getInstance().getSession().getDiaryBeanDao().deleteAll();
        }

        /**
         * 批量插入数据
         * @param datas
         */
        public static void insertDiarys(List<DiaryBean> datas){
            GreenDaoManager.getInstance().getSession().getDiaryBeanDao().insertInTx(datas);
        }
    }
}
