package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.ShouYeFragmentM;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class ShouYeFragmentP extends BasePresenter implements Ipresenter {


    private  IHome iHome;
    private  ShouYeFragmentM shouYeFragmentM;

    public ShouYeFragmentP(IHome iHome ) {
        this.iHome=iHome;

        shouYeFragmentM = new ShouYeFragmentM(this);
    }



    @Override
    public void getCartDataNet(String url, Map<String, String> map) {
        shouYeFragmentM.getCartDataNet(url,map);
    }

    @Override
    public void unsubcribe() {
        //解除订阅
        shouYeFragmentM.unsubcribe();
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

        shouYeFragmentM.getFenLeiData(url);
    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {
        iHome.onFenLeiSuccess(responseBody);
    }

    @Override
    public void onSuccessByQQ(ResponseBody loginBean, String ni_cheng, String iconurl) {

    }


}
