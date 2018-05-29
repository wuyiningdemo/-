package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.RetrofitUtil;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class ShouYeFragmentM  implements  BaseModel{
    private  Ipresenter shouYeFragmentPInter;
    private Disposable d;

    public ShouYeFragmentM(Ipresenter shouYeFragmentPInter) {
        this.shouYeFragmentPInter=shouYeFragmentPInter;

    }



    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

        RetrofitUtil.getService().doGet(Constants.homeUrl,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ShouYeFragmentM.this.d = d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //代表获取到数据
                        shouYeFragmentPInter.onSuccess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //发生错误
                        shouYeFragmentPInter.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void unsubcribe() {
        d.dispose();//解除订阅
    }
    @Override
    public void getFenLeiData(String url) {

        RetrofitUtil.getService().doGet(Constants.fenLeiUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ShouYeFragmentM.this.d = d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //代表获取到数据
                        shouYeFragmentPInter.onFenLeiSuccess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //发生错误
                        shouYeFragmentPInter.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
