package com.work.ifanfan.knowit;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
/**
 * Created by Administrator on 2016/8/30.
 */
public class HttpUtil {


    public HttpUtil(){

    }

    public void getHttp(String mUrl, StringCallback mStringCallback){
        OkHttpUtils
                .get()
                .url(mUrl)
                .build()
                .execute(mStringCallback);
    }



}
