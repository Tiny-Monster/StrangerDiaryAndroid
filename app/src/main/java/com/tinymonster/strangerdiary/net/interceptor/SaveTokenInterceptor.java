package com.tinymonster.strangerdiary.net.interceptor;

import com.tinymonster.strangerdiary.net.netconst.UrlContainer;
import com.tinymonster.strangerdiary.utils.PreUtils;
import com.tinymonster.strangerdiary.utils.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 过滤器 保存token
 * Created by TinyMonster on 10/01/2019.
 */

public class SaveTokenInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response response=chain.proceed(request);
        String token=response.header("token");
        if(!StringUtils.isEmpty(token)&&request.url().toString().endsWith(UrlContainer.LOGIN)){
            PreUtils.put(response.request().url().host(),token);
        }
        return response;
    }
}
