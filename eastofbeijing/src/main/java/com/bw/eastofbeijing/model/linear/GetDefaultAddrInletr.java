package com.bw.eastofbeijing.model.linear;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public interface GetDefaultAddrInletr {
    void  onGetDefaultAddrscuess(ResponseBody responseBody);
    void onError(Throwable throwable);
    void onGetDefaultAddrEmpty();
}
