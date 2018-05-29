package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.LoginModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class LoginPresenter extends BasePresenter implements Ipresenter {
    private  IHome iHome;
    private  LoginModel loginModel;

    public LoginPresenter(IHome iHome) {
        this.iHome=iHome;
        loginModel = new LoginModel(this);
    }

    public void getLogin(String url, String phone, String pwd) {
        loginModel.getLogin(url,phone,pwd);
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
        loginModel.unsubcribe();
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
    public void onSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {


            iHome.getLoginSuccessByQQ(responseBody,ni_cheng,iconurl);


    }

    public void getLoginByQQ(String phone, String pwd, String ni_cheng, String iconurl) {

        loginModel.getLoginByQQ(phone,pwd,ni_cheng,iconurl);

    }
}
