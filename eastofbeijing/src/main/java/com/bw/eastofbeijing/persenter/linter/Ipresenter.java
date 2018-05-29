package com.bw.eastofbeijing.persenter.linter;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/3/14.
 */
public interface Ipresenter {
    void getCartDataNet(String url, Map<String, String> map);
    void unsubcribe();

    void onSuccess(ResponseBody responseBody);
    void onError(Throwable throwable);

    void  getFenLeiData(String url);

    void onFenLeiSuccess(ResponseBody responseBody);

    void onSuccessByQQ(ResponseBody loginBean, String ni_cheng, String iconurl);


}
