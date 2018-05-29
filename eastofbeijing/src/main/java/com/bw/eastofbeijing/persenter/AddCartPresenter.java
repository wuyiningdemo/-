package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.AddCartModel;
import com.bw.eastofbeijing.persenter.linter.IAddCartPLinter;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/21.
 */

public class AddCartPresenter  extends  BasePresenter implements IAddCartPLinter {

    private  IAddCartPLinter iAddCartPLinter;

    private  AddCartModel addCartModel;

    public AddCartPresenter(IAddCartPLinter iAddCartPLinter) {
        this.iAddCartPLinter=iAddCartPLinter;
        addCartModel = new AddCartModel(this);
    }

    public void addToCart(String url, String uid, int pid) {
        addCartModel.addToCart(url,uid,pid);
    }


    @Override
    public void unsubcribe() {
        addCartModel.unsubcribe();//解除订阅
    }

    @Override
    public void onAddCartScuess(ResponseBody responseBody)
    {
        iAddCartPLinter.onAddCartScuess(responseBody);
    }

    @Override
    public void onError(Throwable throwable) {
        iAddCartPLinter.onError(throwable);
    }




}
