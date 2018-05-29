package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.model.linear.CartInlerP;
import com.bw.eastofbeijing.utils.RetrofitUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/24.
 */

public class FragmentCartModel implements  BaseModel{
    private  CartInlerP ipresenter;
    private Disposable d;

    public FragmentCartModel(CartInlerP ipresenter) {
        this.ipresenter=ipresenter;
    }

    public void getCartData(String url, String uid) {
        Map<String, String> map=new HashMap<>();
        map.put("uid",uid);
        RetrofitUtil.getService().doGet(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        FragmentCartModel.this.d=d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String json = responseBody.string();
                            if ("null".equals(json)){
                                ipresenter.getCartDataNull();
                            }else {
                                CartBean  cartBean=new Gson().fromJson(json,CartBean.class);
                                ipresenter.onCartScuess(cartBean);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
