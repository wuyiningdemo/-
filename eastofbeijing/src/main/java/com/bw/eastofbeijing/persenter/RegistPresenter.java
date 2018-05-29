package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.RegistModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class RegistPresenter extends BasePresenter implements Ipresenter {
    private  IHome iHome;
    private final RegistModel registModel;

    public RegistPresenter(IHome iHome) {
        this.iHome=iHome;
        registModel = new RegistModel(this);
    }

    public void registUser(String url, String name, String pwd) {
        registModel.registUser(url,name,pwd);
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
        registModel.unsubcribe();
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
