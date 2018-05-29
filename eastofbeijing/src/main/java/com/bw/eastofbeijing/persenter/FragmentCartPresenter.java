package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.FragmentCartModel;
import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.model.linear.CartInlerP;
import com.bw.eastofbeijing.persenter.linter.CartInlet;

/**
 * Created by 兰昊琼 on 2018/3/24.
 */

public class FragmentCartPresenter extends  BasePresenter implements CartInlerP{
    private  CartInlet cartInlet;

    private  FragmentCartModel fragmentCartModel;

    public FragmentCartPresenter(CartInlet cartInlet) {
        this.cartInlet=cartInlet;
        fragmentCartModel = new FragmentCartModel(this);
    }

    public void getCartData(String url, String uid) {
        fragmentCartModel.getCartData(url,uid);
    }

    @Override
    public void getCartDataNull() {
        cartInlet.getCartDataNull();
    }


    @Override
    public void unsubcribe() {
        fragmentCartModel.unsubcribe();
    }

    @Override
    public void onCartScuess(CartBean cartBean) {
        cartInlet.onCartScuess(cartBean);
    }

    @Override
    public void onError(Throwable throwable) {
        cartInlet.onError(throwable);
    }




}
