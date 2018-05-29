package com.bw.eastofbeijing.persenter.linter;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/24.
 */

public interface IAddCartPLinter {
    void  onAddCartScuess(ResponseBody responseBody);
    void onError(Throwable throwable);
    void unsubcribe();
}
