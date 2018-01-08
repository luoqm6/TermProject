package com.example.termproject.BDFY;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by zachary on 2017/7/6.
 */

public abstract class TransCallBack extends Callback<TransResult> {
    @Override
    public TransResult parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        TransResult result = JSON.parseObject(s, TransResult.class);
        return result;
    }
}
