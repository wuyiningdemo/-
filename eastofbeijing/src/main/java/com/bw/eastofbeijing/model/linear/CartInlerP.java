package com.bw.eastofbeijing.model.linear;

import com.bw.eastofbeijing.model.bean.CartBean;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public interface CartInlerP {
    void  onCartScuess(CartBean cartBean);
    void onError(Throwable throwable);
    void unsubcribe();
    void getCartData(String url, String uid);
    void getCartDataNull();

}
