package com.bw.eastofbeijing.persenter.linter;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public interface ICreateOrderLinter {
   void onOrderDataSuccess(ResponseBody responseBody);
    void onError(Throwable throwable);
}
