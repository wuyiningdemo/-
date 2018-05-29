package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.persenter.linter.GetDefaultAddrPInletr;
import com.bw.eastofbeijing.utils.RetrofitUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public class GetDefaultAddrModel implements  BaseModel{
    private  GetDefaultAddrPInletr getDefaultAddrPInletr;
    private Disposable d;

    public GetDefaultAddrModel(GetDefaultAddrPInletr getDefaultAddrPInletr) {
        this.getDefaultAddrPInletr=getDefaultAddrPInletr;
    }

    public void getDefaultAddr(String url, String uid) {
        Map<String, String> map=new HashMap<>();
        map.put("uid",uid);
        RetrofitUtil.getService().doPost(url,map)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GetDefaultAddrModel.this.d=d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getDefaultAddrPInletr.onGetDefaultAddrscuess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
    d.dispose();
    }

    @Override
    public void getFenLeiData(String url) {

    }
}
