package com.tinymonster.strangerdiary.core.model.impl;

import com.tinymonster.strangerdiary.core.model.IModel;
import com.tinymonster.strangerdiary.net.ApiServer;
import com.tinymonster.strangerdiary.net.RxRetrofit;
import com.tinymonster.strangerdiary.ui.register.RegisterActivity;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class BaseModel implements IModel{
    @Override
    public ApiServer doRxRequest() {
        return RxRetrofit.Api();
    }
}
