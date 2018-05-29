package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.model.linear.ICreateOrderPersenterLinter;
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

public class CreateOrderModel implements  BaseModel{
    private  ICreateOrderPersenterLinter ipresenter;
    private Disposable d;

    public CreateOrderModel(ICreateOrderPersenterLinter ipresenter) {
        this.ipresenter=ipresenter;
    }

    public void createOrder(String url, String uid, double price) {
        Map<String, String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("price", String.valueOf(price));
        RetrofitUtil.getService().doPost(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        CreateOrderModel.this.d=d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        ipresenter.onOrderDataSuccess(responseBody);
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
      d.dispose();//解除订阅
    }

    @Override
    public void getFenLeiData(String url) {

    }
}
