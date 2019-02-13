package com.tinymonster.strangerdiary.net.interceptor;

import com.tinymonster.strangerdiary.utils.PreUtils;
import com.tinymonster.strangerdiary.utils.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class SetTokenInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder=chain.request().newBuilder();
        String token=(String) PreUtils.get("token","");//获取token
        if(!StringUtils.isEmpty(token)){
            builder.addHeader("authorization",token);
        }
        return chain.proceed(builder.build());
    }
}
