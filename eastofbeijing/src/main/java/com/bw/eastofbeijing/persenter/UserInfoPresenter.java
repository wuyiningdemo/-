package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.UserInfoModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class UserInfoPresenter extends BasePresenter implements Ipresenter {

    private  IHome iHome;
    private  UserInfoModel userInfoModel;

    public UserInfoPresenter(IHome iHome) {
        this.iHome=iHome;
        userInfoModel = new UserInfoModel(this);
    }
    public  void getUserInfo(String userInfoUrl, String uid){
        userInfoModel.getUserInfo(userInfoUrl,uid);
    };
    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
        userInfoModel.unsubcribe();
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {

    }

    @Override
    public void onError(Throwable throwable) {

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
