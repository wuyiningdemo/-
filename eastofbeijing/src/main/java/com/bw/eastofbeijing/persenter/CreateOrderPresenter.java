package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.CreateOrderModel;
import com.bw.eastofbeijing.model.linear.ICreateOrderPersenterLinter;
import com.bw.eastofbeijing.persenter.linter.ICreateOrderLinter;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public class CreateOrderPresenter extends BasePresenter implements ICreateOrderPersenterLinter{
    private  ICreateOrderLinter iCreateOrderLinter;

    private CreateOrderModel createOrderModel;

    public CreateOrderPresenter(ICreateOrderLinter iCreateOrderLinter) {
        this.iCreateOrderLinter=iCreateOrderLinter;
        createOrderModel = new CreateOrderModel(this);
    }

    public void createOrder(String url, String uid, double price) {
        createOrderModel.createOrder(url,uid,price);
    }




    @Override
    public void onOrderDataSuccess(ResponseBody responseBody) {
        iCreateOrderLinter.onOrderDataSuccess(responseBody);
    }

    @Override
    public void onError(Throwable throwable) {
        iCreateOrderLinter.onError(throwable);
    }






}
