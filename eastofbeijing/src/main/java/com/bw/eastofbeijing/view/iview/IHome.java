package com.bw.eastofbeijing.view.iview;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public interface IHome {
    void onSuccess(ResponseBody responseBody) ;
    void onError(Throwable throwable);
    void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl);
    void onFenLeiSuccess(ResponseBody responseBody) ;

    void  getCartDataNull();
}
