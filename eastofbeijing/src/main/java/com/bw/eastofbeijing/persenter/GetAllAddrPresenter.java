package com.bw.eastofbeijing.persenter;


import com.bw.eastofbeijing.model.GetAllAddrModel;
import com.bw.eastofbeijing.model.linear.GetAllAddrInter;
import com.bw.eastofbeijing.persenter.linter.GetAllAddrPresenterInter;

import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/2/27.
 */
public class GetAllAddrPresenter implements GetAllAddrPresenterInter {

    private GetAllAddrInter getAllAddrInter;
    private GetAllAddrModel getAllAddrModel;

    public GetAllAddrPresenter(GetAllAddrInter getAllAddrInter) {
        this.getAllAddrInter = getAllAddrInter;
        getAllAddrModel = new GetAllAddrModel(this);
    }

    public void getAllAddr(String getAllAddrUrl, String uid) {
        getAllAddrModel.getAllAddr(getAllAddrUrl,uid);
    }

    @Override
    public void onGetAllAddrSuccess(ResponseBody responseBody) {
        getAllAddrInter.onGetAllAddrSuccess(responseBody);
    }
}
