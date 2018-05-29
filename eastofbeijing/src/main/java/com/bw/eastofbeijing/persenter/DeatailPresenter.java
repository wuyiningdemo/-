package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.DeatailModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/21.
 */

public class DeatailPresenter extends   BasePresenter implements Ipresenter {

    private  IHome iHome;
    private  DeatailModel deatailModel;

    public DeatailPresenter(IHome iHome) {
        this.iHome=iHome;
        deatailModel = new DeatailModel(this);
    }

    public void getDetailData(String url, int pid) {
        deatailModel.getDetailData(url,pid);
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
        //解除订阅
        deatailModel.unsubcribe();
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        iHome.onSuccess(responseBody);
    }

    @Override
    public void onError(Throwable throwable) {
        iHome.onError(throwable);
    }

    @Override
    public void getFenLeiData(String url) {

    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void onSuccessByQQ(ResponseBody loginBean, String ni_cheng, String iconurl) {

    }
}
