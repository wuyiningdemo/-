package com.bw.eastofbeijing.persenter.linter;

import com.bw.eastofbeijing.model.bean.CartBean;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public interface CartInlet {
    void  onCartScuess(CartBean cartBean);
    void onError(Throwable throwable);
    void getCartDataNull();
}
