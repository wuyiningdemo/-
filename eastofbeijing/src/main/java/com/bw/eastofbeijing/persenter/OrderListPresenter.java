package com.bw.eastofbeijing.persenter;


import com.bw.eastofbeijing.model.OrderListModel;
import com.bw.eastofbeijing.model.linear.FragmentOrderListInter;
import com.bw.eastofbeijing.persenter.linter.OrderListPresenterInter;

import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/2/25.
 */
public class OrderListPresenter implements OrderListPresenterInter {

    private FragmentOrderListInter fragmentOrderListInter;
    private OrderListModel orderListModel;

    public OrderListPresenter(FragmentOrderListInter fragmentOrderListInter) {
        this.fragmentOrderListInter = fragmentOrderListInter;
        orderListModel = new OrderListModel(this);
    }

    public void getOrderData(String url, String uid, int page) {

        orderListModel.getOrderData(url,uid,page);

    }

    @Override
    public void onOrderDataSuccess(ResponseBody responseBody) {

        fragmentOrderListInter.onOrderDataSuccess(responseBody);
    }
}
