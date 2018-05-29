package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.AddNewAddrModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public class AddNewAddrPresenter extends BasePresenter implements Ipresenter{
    private  IHome iHome;
    private final AddNewAddrModel addNewAddrModel;

    public AddNewAddrPresenter(IHome iHome) {
        this.iHome=iHome;
        addNewAddrModel = new AddNewAddrModel(this);
    }

    public void addNewAddr(String url, String uid, String addr, String mobile, String name) {
        addNewAddrModel.addNewAddr(url,uid,addr,mobile,name);
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
     addNewAddrModel.unsubcribe();
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
