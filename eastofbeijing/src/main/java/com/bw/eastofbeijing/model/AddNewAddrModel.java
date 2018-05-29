package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.utils.RetrofitUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public class AddNewAddrModel implements  BaseModel{
    private  Ipresenter ipresenter;
    private Disposable d;

    public AddNewAddrModel(Ipresenter ipresenter) {
        this.ipresenter=ipresenter;
    }

    public void addNewAddr(String url, String uid, String addr, String mobile, String name) {
        Map<String, String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("addr",addr);
        map.put("mobile",mobile);
        map.put("name",name);
        RetrofitUtil.getService().doPost(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        AddNewAddrModel.this.d=d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                     ipresenter.onSuccess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ipresenter.onError(e);
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
