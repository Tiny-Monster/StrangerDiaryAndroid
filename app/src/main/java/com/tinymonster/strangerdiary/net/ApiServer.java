package com.tinymonster.strangerdiary.net;

import com.mob.wrappers.UMSSDKWrapper;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.bean.ImagesUrlBean;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.net.netconst.UrlContainer;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


/**
 * Created by TinyMonster on 10/01/2019.
 */

public interface ApiServer {
    @FormUrlEncoded
    @POST(UrlContainer.LOGIN)
    Observable<BaseBean<UserBean>> login(@Field("account") String username,@Field("password") String password);

    @FormUrlEncoded
    @POST(UrlContainer.REGISTER)
    Observable<BaseBean<UserBean>> register(@Field("account") String username,@Field("password") String password);

    @FormUrlEncoded
    @POST(UrlContainer.LOGOUT)
    Observable<BaseBean> logout();

    @Multipart
    @POST(UrlContainer.UPLOADPIC)
    Observable<BaseBean<ImagesUrlBean>> UploadImage(@Part List<MultipartBody.Part> parts);


    @POST(UrlContainer.UPLOADDIARY)
    Observable<BaseBean<DiaryBean>> UploadDiary(@Body RequestBody body);

    @FormUrlEncoded
    @POST(UrlContainer.DOWNLOADALLDATA)
    Observable<BaseBean<List<DiaryBean>>> downloadAllDiary(@Field("userId") Long userId);

    @POST(UrlContainer.UPDATEDIARY)
    Observable<BaseBean<DiaryBean>> UpdateDiary(@Body RequestBody body);
}
