package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.ProductListModel;
import com.bw.eastofbeijing.persenter.linter.Ipresenter;
import com.bw.eastofbeijing.view.iview.IHome;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/24.
 */

public class ProductListPresenter extends  BasePresenter implements Ipresenter{
    private  IHome iHome;
    private  ProductListModel productListModel;

    public ProductListPresenter(IHome iHome) {
       this. iHome=iHome;
        productListModel = new ProductListModel(this);
    }

    public void getProductData(String seartchUrl, String keywords, int page) {
        productListModel.getProductData(seartchUrl,keywords,page);
    }

    @Override
    public void getCartDataNet(String url, Map<String, String> map) {

    }

    @Override
    public void unsubcribe() {
        productListModel.unsubcribe();
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
