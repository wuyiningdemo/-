package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.GetDefaultAddrModel;
import com.bw.eastofbeijing.model.linear.GetDefaultAddrInletr;
import com.bw.eastofbeijing.persenter.linter.GetDefaultAddrPInletr;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/25.
 */

public class GetDefaultAddrPresenter extends  BasePresenter implements GetDefaultAddrPInletr {


    private  GetDefaultAddrInletr getDefaultAddrInletr;
    private final GetDefaultAddrModel getDefaultAddrModel;

    public GetDefaultAddrPresenter(GetDefaultAddrInletr getDefaultAddrInletr) {
        this.getDefaultAddrInletr=getDefaultAddrInletr;
        getDefaultAddrModel = new GetDefaultAddrModel(this);
    }

    public void getDefaultAddr(String url, String uid) {
        getDefaultAddrModel.getDefaultAddr(url,uid);
    }


    @Override
    public void onGetDefaultAddrscuess(ResponseBody responseBody) {
        getDefaultAddrInletr.onGetDefaultAddrscuess(responseBody);
    }


}
