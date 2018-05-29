package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.model.linear.BaseAddModel;
import com.bw.eastofbeijing.persenter.linter.IAddCartPLinter;
import com.bw.eastofbeijing.utils.RetrofitUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class AddCartModel implements BaseAddModel{
    private  IAddCartPLinter ipresenter;
    private Disposable d;

    public AddCartModel(IAddCartPLinter ipresenter) {
        this.ipresenter=ipresenter;
    }

    public void addToCart(String url, String uid, int pid) {
        Map<String, String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("pid", String.valueOf(pid));
        RetrofitUtil.getService().doGet(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<ResponseBody>() {
                   @Override
                   public void onSubscribe(Disposable d) {
                       AddCartModel.this.d=d;
                   }

                   @Override
                   public void onNext(ResponseBody responseBody) {
                    ipresenter.onAddCartScuess(responseBody);
                   }

                   @Override
                   public void onError(Throwable e) {
                       ipresenter.onError(e);
                   }

                   @Override
                   public void onComplete() {

                   }
               })



        ;
    }



    @Override
    public void unsubcribe() {
        d.dispose();//解除订阅
    }


}
