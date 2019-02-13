package com.tinymonster.strangerdiary.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public class EventBus {
    private static EventBus mInstance;
    private Map<String,List<PublishSubject>> mSubjectMap=new HashMap<>();//使用观察者模式，保存所有的注册事件的对象

    /**
     * 单例模式的实现
     * @return
     */
    public static EventBus getInstance(){
        if(mInstance==null){
            synchronized (EventBus.class){
                if(mInstance==null){
                    mInstance=new EventBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 向指定事件线中注册事件
     * @param eventAction
     * @return
     */
    public PublishSubject registerEvent(String eventAction){
        List<PublishSubject> list=mSubjectMap.get(eventAction);
        if(list==null){
            list=new ArrayList<PublishSubject>();
            mSubjectMap.put(eventAction,list);
        }
        PublishSubject mSubject = PublishSubject.create();
        list.add(mSubject);
        return mSubject;
    }

    /**
     * 向指定事件线中发送事件
     * @param eventAction
     * @param object
     */
    public void postEvent(String eventAction,Object object){
        List<PublishSubject> list=mSubjectMap.get(eventAction);
        if(list!=null&&!list.isEmpty()){
            for(PublishSubject publishSubject:list){
                publishSubject.onNext(object);
            }
        }
    }

    /**
     * 解除事件
     * @param eventAction
     * @param publishSubject
     * @param observer
     */
    public void unRegisterEvent(String eventAction, PublishSubject publishSubject, DisposableObserver observer){
        List<PublishSubject> list=mSubjectMap.get(eventAction);
        if(observer!=null&&!observer.isDisposed()){
            observer.dispose();//停止异步事件
        }
        if(list!=null){
            list.remove(publishSubject);
        }
        if(list!=null&&list.isEmpty()){
            mSubjectMap.remove(eventAction);
        }
    }
}
